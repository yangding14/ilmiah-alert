package com.ilmiah.ilmiah_alert.monitoring;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DockerComposeRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("docker", "compose", "up", "-d");
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Failed to start Docker Compose");
        }
    }
}
