package com.example.pedido_db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class PedidoDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(PedidoDbApplication.class, args);
    }

}
