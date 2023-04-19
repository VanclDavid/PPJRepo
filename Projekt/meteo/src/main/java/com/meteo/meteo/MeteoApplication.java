package com.meteo.meteo;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeteoApplication {
	private static final Logger logger = Logger.getLogger(MeteoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MeteoApplication.class, args);
		logger.info("Application started");
	}
}
