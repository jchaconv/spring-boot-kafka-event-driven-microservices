package com.vilelo.ws.transfers_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TransfersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransfersServiceApplication.class, args);
	}

    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
