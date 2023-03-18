package com.zitt.cdx.controller;

import com.zitt.cdx.domain.Permit;
import com.zitt.cdx.service.CoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author lukasz
 * @since 2022-05-08
 */
@RestController
public class CoordinatorController {

    @Autowired
    private CoordinatorService redAdminService;

    @GetMapping("/permission/{resourceId}")
    public ResponseEntity<Permit> getPermission(@PathVariable String resourceId) {
        Permit permit = redAdminService.getPermission(resourceId);
        return ResponseEntity.ok().body(permit);
    }

    @PostMapping("/release/{resourceId}/{permitId}")
    public ResponseEntity<Permit> expirePermission(@PathVariable String resourceId,
                                                   @PathVariable String permitId) {
        redAdminService.expire(resourceId, permitId);
        return ResponseEntity.ok().build();
    }


    @ExceptionHandler({HttpClientErrorException.BadRequest.class})
    public ResponseEntity<Object> handleError() {
        return new ResponseEntity<>(
                "Bad request", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpClientErrorException.NotFound.class})
    public ResponseEntity<Object> handle404Error() {
        return new ResponseEntity<>(
                "Not found", new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

}
