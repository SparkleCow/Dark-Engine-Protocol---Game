package com.sparklecow.dark_engine_protocol_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DarkEngineProtocolAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(DarkEngineProtocolAuthApplication.class, args);
	}
}
