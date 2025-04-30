package com.finner.integration.staah_integration.Runner;

import com.finner.integration.staah_integration.Service.PollingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final PollingService pollingService;

    public StartupRunner(PollingService pollingService) {
        this.pollingService = pollingService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialization logic, e.g., start polling as soon as the application starts
        pollingService.pollStaahAndProcess();
    }
}