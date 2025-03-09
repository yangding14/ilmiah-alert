package com.ilmiah.ilmiah_alert.service;

import com.ilmiah.ilmiah_alert.config.AlertSubcriberList;
import com.ilmiah.ilmiah_alert.external.alert.AlertClient;
import com.ilmiah.ilmiah_alert.external.ilmiah.dto.ProjectData;
import com.ilmiah.ilmiah_alert.model.Department;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AlertService {
    private final AlertClient alertClient;
    private final AlertSubcriberList alertSubcriberList;
    private final String adminTelegramId;

    public AlertService(
            AlertClient alertClient,
            AlertSubcriberList alertSubcriberList,
            @Value("${alert.admin.telegram}") String adminTelegramId) {
        this.alertClient = alertClient;
        this.alertSubcriberList = alertSubcriberList;
        this.adminTelegramId = adminTelegramId;
    }

    public void alertSubscribers(
            Department department,
            List<ProjectData> addedProjectData,
            List<ProjectData> removedProjectData) {
        alertSubcriberList
                .getSubscribers(department)
                .forEach(
                        subscriberId -> {
                            alertClient.sendAlert(
                                    subscriberId,
                                    resolveMessage(
                                            department, addedProjectData, removedProjectData));
                        });
    }

    private String resolveMessage(
            Department department,
            List<ProjectData> addedProjectData,
            List<ProjectData> removedProjectData) {
        StringBuilder builder =
                new StringBuilder().append("Department ").append(department.name()).append(" has ");

        if (!addedProjectData.isEmpty()) {
            builder.append(addedProjectData.size());
            builder.append(" new projects:\n");
            for (int i = 0; i < addedProjectData.size(); i++) {
                builder.append(i + 1)
                        .append(". ")
                        .append(addedProjectData.get(i).title())
                        .append("\n");
            }
        }

        if (!removedProjectData.isEmpty()) {
            if (!addedProjectData.isEmpty()) {
                builder.append("and ");
            }
            builder.append(removedProjectData.size()).append(" projects removed:\n");
            for (int i = 0; i < removedProjectData.size(); i++) {
                builder.append(i + 1)
                        .append(". ")
                        .append(removedProjectData.get(i).title())
                        .append("\n");
            }
        }

        return builder.toString();
    }

    public void alertAdmin(String message) {
        alertClient.sendAlert(adminTelegramId, message);
    }

    @Scheduled(initialDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void applicationStartupAlert() {
        alertClient.sendAlert(adminTelegramId, "Application started");
    }
}
