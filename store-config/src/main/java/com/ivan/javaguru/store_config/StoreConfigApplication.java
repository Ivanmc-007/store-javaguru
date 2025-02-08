package com.ivan.javaguru.store_config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class StoreConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreConfigApplication.class, args);
	}

}
