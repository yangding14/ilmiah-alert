package com.ilmiah.ilmiah_alert.service.checker;

import com.ilmiah.ilmiah_alert.external.ilmiah.IlmiahClient;
import com.ilmiah.ilmiah_alert.model.Department;
import com.ilmiah.ilmiah_alert.service.AlertService;
import com.ilmiah.ilmiah_alert.service.IlmiahUpdateCheckerService;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "checker.software-engineering.enabled", havingValue = "true")
public class SoftwareEngineeringCheckerService extends IlmiahUpdateCheckerService {

    public SoftwareEngineeringCheckerService(IlmiahClient ilmiahClient, AlertService alertService) {
        super(
                ilmiahClient,
                alertService,
                LoggerFactory.getLogger(SoftwareEngineeringCheckerService.class));
    }

    @Override
    public Department getDepartment() {
        return Department.SOFTWARE_ENGINEERING;
    }
}
