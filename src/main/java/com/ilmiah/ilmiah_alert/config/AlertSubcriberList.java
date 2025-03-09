package com.ilmiah.ilmiah_alert.config;

import com.ilmiah.ilmiah_alert.model.Department;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AlertSubcriberList {

    private final Map<Department, List<String>> departmentSubscribers = new ConcurrentHashMap<>();

    public AlertSubcriberList() {
        initializeSubscribers();
    }

    private void initializeSubscribers() {
        // Initialize lists for all departments
        Arrays.stream(Department.values())
                .forEach(dept -> departmentSubscribers.put(dept, new ArrayList<>()));

        // Load subscribers from CSV file
        Resource resource = new ClassPathResource("alert-subscriber-list.csv");
        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String platform = parts[0];
                    String departmentStr = parts[2];
                    String subscriberId = parts[3];

                    if ("telegram".equalsIgnoreCase(platform)) {
                        if ("all".equalsIgnoreCase(departmentStr)) {
                            // Add this subscriber to all departments
                            departmentSubscribers.forEach((dept, list) -> list.add(subscriberId));
                        } else {
                            Department department = mapStringToDepartment(departmentStr);
                            if (department != null) {
                                departmentSubscribers.get(department).add(subscriberId);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load subscriber list from CSV", e);
        }
    }

    private Department mapStringToDepartment(String departmentStr) {
        switch (departmentStr.toLowerCase().replace("-", "_")) {
            case "software_engineering":
                return Department.SOFTWARE_ENGINEERING;
            case "computer_system_and_network":
                return Department.COMPUTER_SYSTEM_AND_NETWORK;
            case "multimedia":
                return Department.MULTIMEDIA;
            case "artificial_intelligence":
                return Department.ARTIFICIAL_INTELLIGENCE;
            case "information_system":
                return Department.INFORMATION_SYSTEM;
            case "islamic_studies":
                return Department.ISLAMIC_STUDIES;
            default:
                return null;
        }
    }

    public List<String> getSubscribers(Department department) {
        return new ArrayList<>(departmentSubscribers.get(department));
    }
}
