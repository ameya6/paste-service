package org.paste.controller;

import lombok.extern.log4j.Log4j2;
import org.data.model.entity.SystemHealth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Log4j2
public class HealthController {

    @GetMapping("")
    public ResponseEntity<SystemHealth> health() {
        SystemHealth systemHealth = SystemHealth.builder().build();
        try {
            systemHealth.setMessage("Healthy");
            return ResponseEntity.ok(systemHealth);
        } catch (Exception e) {
            log.error("Exception :" + e.getMessage(), e);
            systemHealth.setMessage("System Failure");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(systemHealth);
        }
    }
}
