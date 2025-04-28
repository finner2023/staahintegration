package com.finner.integration.staah_integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class StaahIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaahIntegrationApplication.class, args);
	}

}
