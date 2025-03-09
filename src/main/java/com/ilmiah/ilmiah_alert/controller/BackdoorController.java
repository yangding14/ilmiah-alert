package com.ilmiah.ilmiah_alert.controller;

import com.ilmiah.ilmiah_alert.external.ilmiah.IlmiahClient;
import com.ilmiah.ilmiah_alert.external.ilmiah.dto.GetProjectListResp;
import com.ilmiah.ilmiah_alert.model.Department;
import com.ilmiah.ilmiah_alert.model.IlmiahApiException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackdoorController {

    private final IlmiahClient ilmiahClient;

    public BackdoorController(IlmiahClient ilmiahClient) {
        this.ilmiahClient = ilmiahClient;
    }

    @GetMapping("/test")
    public ResponseEntity<String> backdoor() {
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/getProjectList")
    public ResponseEntity<GetProjectListResp> getProjectList(
            @RequestParam("department") Department department) throws IlmiahApiException {
        return ResponseEntity.ok(ilmiahClient.getProjectList(department));
    }
}
