package com.ilmiah.ilmiah_alert.controller;

import com.ilmiah.ilmiah_alert.external.ilmiah.IlmiahClient;
import com.ilmiah.ilmiah_alert.external.ilmiah.dto.GetProjectListResp;
import com.ilmiah.ilmiah_alert.external.ilmiah.dto.ProjectData;
import com.ilmiah.ilmiah_alert.model.Department;
import com.ilmiah.ilmiah_alert.service.AlertService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BackdoorController {

    private final IlmiahClient ilmiahClient;
    private final AlertService alertService;

    public BackdoorController(IlmiahClient ilmiahClient, AlertService alertService) {
        this.ilmiahClient = ilmiahClient;
        this.alertService = alertService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> backdoor() {
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/getProjectList")
    public ResponseEntity<GetProjectListResp> getProjectList(
            @RequestParam("department") Department department) {
        return ResponseEntity.ok(ilmiahClient.getProjectList(department));
    }

    @PostMapping("/sendAlert")
    public ResponseEntity<String> sendAlert(
            @RequestParam("department") Department department,
            @RequestBody BackdoorProjectDataReq projectData) {
        alertService.alertSubscribers(
                department, projectData.addedProjectData, projectData.removedProjectData);
        return ResponseEntity.ok("Success");
    }

    public record BackdoorProjectDataReq(
            List<ProjectData> addedProjectData, List<ProjectData> removedProjectData) {}
}
