package com.ilmiah.ilmiah_alert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IlmiahAlertApplication {

    public static void main(String[] args) {
        SpringApplication.run(IlmiahAlertApplication.class, args);
    }
}
