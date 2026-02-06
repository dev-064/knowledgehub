package com.divyanshu.knowledgehub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CognitexApplication {
	private static final Logger log = LoggerFactory.getLogger(CognitexApplication.class);
	public static void main(String[] args) {
		log.info("Starting Cognitex application");
		SpringApplication.run(CognitexApplication.class, args);
		log.info("Cognitex application started");
	}

}
