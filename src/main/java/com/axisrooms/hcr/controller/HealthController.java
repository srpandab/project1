
package com.axisrooms.hcr.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<?> health() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body("OK");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DOWN");
        }
    }
}