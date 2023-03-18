package com.zitt.cdx.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    private final String REDIS_ADDRESS;
    private final String REDIS_PORT;
    private final TransportMode REDIS_TRANSPORT_MODE = TransportMode.NIO;


    /**
     *
     * @param redisUrl
     * @param redisPort
     */
    public RedisConfig(@Value("${REDIS_URL}") String redisUrl,
                       @Value("${REDIS_PORT}") String redisPort) {
        this.REDIS_ADDRESS = redisUrl;
        this.REDIS_PORT = redisPort;
    }

    @Bean
    public RedissonClient createRedissonClient() {
        Config config = new Config();
        //.addNodeAddress("redis://127.0.0.1:7181");
        config.setTransportMode(REDIS_TRANSPORT_MODE)
                .useSingleServer()
                .setAddress(REDIS_ADDRESS + ":" + REDIS_PORT);

        // or read config from file
        // config = Config.fromYAML(new File("redis-config.yaml"));

        // Sync and Async API
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
