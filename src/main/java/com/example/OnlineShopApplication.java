package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.config.StorageProperties;
import com.example.service.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class OnlineShopApplication{

	public static void main(String[] args) {
		SpringApplication.run(OnlineShopApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args -> {
			storageService.init();
		});
	}
}
