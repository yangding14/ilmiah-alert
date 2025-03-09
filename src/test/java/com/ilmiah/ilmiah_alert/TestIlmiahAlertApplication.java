package com.ilmiah.ilmiah_alert;

import org.springframework.boot.SpringApplication;

public class TestIlmiahAlertApplication {

    public static void main(String[] args) {
        SpringApplication.from(IlmiahAlertApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
