package com.wu.duplicatecheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
@ConfigurationPropertiesScan
public class DuplicatecheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuplicatecheckApplication.class, args);
	}

}
