package com.crg.todo.healthcheck;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @CrossOrigin(origins = "*")
    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("I'm good");
    }
}
