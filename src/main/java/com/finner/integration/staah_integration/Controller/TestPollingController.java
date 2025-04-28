package com.finner.integration.staah_integration.Controller;

import com.finner.integration.staah_integration.Service.PollingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestPollingController {

    private final PollingService pollingService;

    public TestPollingController(PollingService pollingService) {
        this.pollingService = pollingService;
    }

    @GetMapping("/poll-now")
    public String triggerPolling() {
        pollingService.pollStaahAndProcess();
        return "✅ Polling triggered manually!";
    }
}
/*
* let's get the shit done in 3 hours
* just to yourself show this is ABHISHEK SINGHAL
*
* */