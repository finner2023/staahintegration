package com.finner.integration.staah_integration.Runner;

import com.finner.integration.staah_integration.Service.PollingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupRunner implements CommandLineRunner {

    private final PollingService pollingService;

    public StartupRunner(PollingService pollingService) {
        this.pollingService = pollingService;
    }

    @Override
    public void run(String... args) {
        log.info("🚀 StartupRunner triggered — initiating first reservation poll manually...");
        pollingService.pollStaahAndProcess().subscribe();  // Ensure reactive chain runs
    }
}
