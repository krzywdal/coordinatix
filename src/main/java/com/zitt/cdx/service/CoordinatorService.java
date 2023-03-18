package com.zitt.cdx.service;

import com.zitt.cdx.domain.Permit;

import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class CoordinatorService {

    private static Logger LOG = LoggerFactory.getLogger(CoordinatorService.class);

    @Value("${MAX_ALLOWED_PERMITS}")
    private int MAX_ALLOWED_PERMITS;
    @Value("${MAX_WAIT_TIME_MS}")
    private long MAX_WAIT_TIME_MS;
    @Value("${PERMIT_EXPIRATION_MS}")
    private Long PERMIT_EXPIRATION_MS;

    @Autowired
    private RedissonClient redissonClient;


    /**
     * @param resourceId
     * @return
     */
    public Permit getPermission(String resourceId) {

        RPermitExpirableSemaphore semaphore = redissonClient.getPermitExpirableSemaphore(resourceId);
        LOG.info("Trying to acquire permission to access {}", resourceId);

        Permit response = new Permit.Builder()
                .permitId(null)
                .resourceId(resourceId)
                .currentPermits(semaphore.availablePermits())
                .maxAllowedPermits(MAX_ALLOWED_PERMITS)
                .isGranted(false)
                .build();

        semaphore.trySetPermitsAsync(MAX_ALLOWED_PERMITS);

        try {
            String permitId = semaphore.tryAcquire(MAX_WAIT_TIME_MS, TimeUnit.MILLISECONDS);
            if (permitId != null) {
                response.setGranted(true);
                response.setPermitId(permitId);
                response.setCurrentPermits(MAX_ALLOWED_PERMITS - semaphore.availablePermits());
                if (PERMIT_EXPIRATION_MS != null & PERMIT_EXPIRATION_MS > 0) {
                    LOG.info("Setting {}ms timeout for permitId: {}", PERMIT_EXPIRATION_MS, permitId);
                    semaphore.expireAsync(Duration.ofMillis(PERMIT_EXPIRATION_MS));
                    response.setExpirationMs(PERMIT_EXPIRATION_MS);
                }
                LOG.info("Permission to access {} granted", resourceId);
            } else {
                LOG.info("Permission to access {} NOT granted", resourceId);
                if (PERMIT_EXPIRATION_MS != null & PERMIT_EXPIRATION_MS > 0) {
                    response.setExpirationMs(PERMIT_EXPIRATION_MS);
                }
                response.setCurrentPermits(MAX_ALLOWED_PERMITS - semaphore.availablePermits());
            }

        } catch (InterruptedException e) {
            LOG.error("ERROR: {}", e.getMessage());

        }
        return response;
    }

    /**
     * @param resourceId
     * @param permitId
     * @return
     */
    public boolean expire(String resourceId, String permitId) {
        try {
            RPermitExpirableSemaphore semaphore = redissonClient.getPermitExpirableSemaphore(resourceId);
            semaphore.release(permitId);
            LOG.info("Released resource's: {} lock with permitId {}", resourceId, permitId);
        } catch (Exception e) {
            LOG.info("ERROR: {}", e.getMessage());
        }
        return true;
    }
}
