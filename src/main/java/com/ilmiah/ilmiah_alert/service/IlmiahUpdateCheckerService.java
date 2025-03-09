package com.ilmiah.ilmiah_alert.service;

import com.ilmiah.ilmiah_alert.external.ilmiah.IlmiahClient;
import com.ilmiah.ilmiah_alert.external.ilmiah.dto.GetProjectListResp;
import com.ilmiah.ilmiah_alert.external.ilmiah.dto.ProjectData;
import com.ilmiah.ilmiah_alert.model.Department;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class IlmiahUpdateCheckerService {
    private final Logger logger;
    private final IlmiahClient ilmiahClient;
    private final AlertService alertService;
    private GetProjectListResp currentProjectListResp;
    private boolean isAlertSent = false;

    public IlmiahUpdateCheckerService(
            IlmiahClient ilmiahClient, AlertService alertService, Logger logger) {
        this.ilmiahClient = ilmiahClient;
        this.alertService = alertService;
        this.logger = logger;
    }

    @Scheduled(fixedRate = 120, timeUnit = TimeUnit.SECONDS)
    public void checkForUpdates() {
        try {
            if (currentProjectListResp == null) {
                currentProjectListResp = ilmiahClient.getProjectList(getDepartment());
            } else {
                GetProjectListResp newProjectListResp =
                        ilmiahClient.getProjectList(getDepartment());
                if (isChanged(newProjectListResp)) {
                    logger.atInfo()
                            .setMessage("Project changes detected from ilmiah")
                            .addKeyValue("department", getDepartment())
                            .addKeyValue("addedProjects", getAddedProjects(newProjectListResp))
                            .addKeyValue("removedProjects", getRemovedProjects(newProjectListResp))
                            .log();
                    alertService.alertSubscribers(
                            getDepartment(),
                            getAddedProjects(newProjectListResp),
                            getRemovedProjects(newProjectListResp));
                    currentProjectListResp = newProjectListResp;
                    isAlertSent = false;
                }
            }
        } catch (Exception e) {
            logger.atError()
                    .setMessage("An error occurred while checking for updates from ilmiah.")
                    .setCause(e)
                    .addKeyValue("department", getDepartment())
                    .log();
            if (!isAlertSent && getDepartment() == Department.ARTIFICIAL_INTELLIGENCE) {
                alertService.alertAdmin(e.getMessage());
                isAlertSent = true;
            }
        }
    }

    private boolean isChanged(GetProjectListResp newProjectListResp) {
        return !newProjectListResp.data().equals(currentProjectListResp.data());
    }

    private List<ProjectData> getAddedProjects(GetProjectListResp newProjectData) {
        return newProjectData.data().stream()
                .filter(project -> !currentProjectListResp.data().contains(project))
                .toList();
    }

    private List<ProjectData> getRemovedProjects(GetProjectListResp newProjectData) {
        return currentProjectListResp.data().stream()
                .filter(project -> !newProjectData.data().contains(project))
                .toList();
    }

    public abstract Department getDepartment();
}
