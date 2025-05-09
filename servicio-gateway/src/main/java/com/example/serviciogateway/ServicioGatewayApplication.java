package com.example.serviciogateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServicioGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioGatewayApplication.class, args);
	}

}
