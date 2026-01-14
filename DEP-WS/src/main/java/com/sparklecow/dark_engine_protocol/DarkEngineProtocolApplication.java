package com.sparklecow.dark_engine_protocol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DarkEngineProtocolApplication {

	public static void main(String[] args) {
		SpringApplication.run(DarkEngineProtocolApplication.class, args);
	}

}
