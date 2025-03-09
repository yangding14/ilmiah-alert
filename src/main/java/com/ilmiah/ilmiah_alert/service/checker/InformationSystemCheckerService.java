package com.ilmiah.ilmiah_alert.service.checker;

import com.ilmiah.ilmiah_alert.external.ilmiah.IlmiahClient;
import com.ilmiah.ilmiah_alert.model.Department;
import com.ilmiah.ilmiah_alert.service.AlertService;
import com.ilmiah.ilmiah_alert.service.IlmiahUpdateCheckerService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InformationSystemCheckerService extends IlmiahUpdateCheckerService {

    public InformationSystemCheckerService(IlmiahClient ilmiahClient, AlertService alertService) {
        super(
                ilmiahClient,
                alertService,
                LoggerFactory.getLogger(InformationSystemCheckerService.class));
    }

    @Override
    public Department getDepartment() {
        return Department.INFORMATION_SYSTEM;
    }
}
