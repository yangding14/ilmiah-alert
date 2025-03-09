package com.ilmiah.ilmiah_alert.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackdoorController {

    @GetMapping("/test")
    public ResponseEntity<String> backdoor() {
        return ResponseEntity.ok("Success");
    }
}
