package com.CS6650.CentralManagementService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CentralManagementServiceApplication {

	public static void main(String[] args) {
		ServerLogger.init(String.valueOf(8081), "CentralManagementServerLog");
		SpringApplication.run(CentralManagementServiceApplication.class, args);
	}

	@Bean
	public ServerManager initializeServerInfo() {
		return new ServerManager();
	}

}
