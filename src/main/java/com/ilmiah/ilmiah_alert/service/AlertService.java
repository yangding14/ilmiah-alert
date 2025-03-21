package com.ilmiah.ilmiah_alert.service;

import com.ilmiah.ilmiah_alert.config.AlertSubcriberList;
import com.ilmiah.ilmiah_alert.external.alert.DiscordAlertClient;
import com.ilmiah.ilmiah_alert.external.alert.TelegramAlertClient;
import com.ilmiah.ilmiah_alert.external.ilmiah.dto.ProjectData;
import com.ilmiah.ilmiah_alert.model.Department;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AlertService {
    private final TelegramAlertClient alertClient;
    private final DiscordAlertClient discordAlertClient;
    private final AlertSubcriberList alertSubcriberList;
    private final String adminTelegramId;

    public AlertService(
            TelegramAlertClient alertClient,
            DiscordAlertClient discordAlertClient,
            AlertSubcriberList alertSubcriberList,
            @Value("${alert.admin.telegram}") String adminTelegramId) {
        this.alertClient = alertClient;
        this.discordAlertClient = discordAlertClient;
        this.alertSubcriberList = alertSubcriberList;
        this.adminTelegramId = adminTelegramId;
    }

    public void alertSubscribers(
            Department department,
            List<ProjectData> addedProjectData,
            List<ProjectData> removedProjectData) {
        discordAlertClient.sendAlert(
                null, resolveMessage(department, addedProjectData, removedProjectData));
        alertAdmin(resolveMessage(department, addedProjectData, removedProjectData));
        //        alertSubcriberList
        //                .getSubscribers(department)
        //                .forEach(
        //                        subscriberId -> {
        //                            alertClient.sendAlert(
        //                                    subscriberId,
        //                                    resolveMessage(
        //                                            department, addedProjectData,
        // removedProjectData));
        //                        });
    }

    private String resolveMessage(
            Department department,
            List<ProjectData> addedProjectData,
            List<ProjectData> removedProjectData) {
        StringBuilder builder =
                new StringBuilder()
                        .append(">> **")
                        .append(department.departmentName())
                        .append("** << \n");

        if (!addedProjectData.isEmpty()) {
            builder.append("\n").append(addedProjectData.size()).append(" project(s) added:\n\n");
            for (int i = 0; i < addedProjectData.size(); i++) {
                builder.append(i + 1)
                        .append(". **")
                        .append(addedProjectData.get(i).title())
                        .append("** (Supervisor: ")
                        .append(addedProjectData.get(i).supervisor())
                        .append(")")
                        .append("\n");
            }
        }

        if (!removedProjectData.isEmpty()) {
            builder.append("\n")
                    .append(removedProjectData.size())
                    .append(" project(s) removed:\n\n");
            for (int i = 0; i < removedProjectData.size(); i++) {
                builder.append(i + 1)
                        .append(". **")
                        .append(removedProjectData.get(i).title())
                        .append("** (Supervisor: ")
                        .append(removedProjectData.get(i).supervisor())
                        .append(")")
                        .append("\n");
            }
        }

        builder.append("\n########END_OF_MESSAGE########");

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
