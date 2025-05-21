package com.example.producto_db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProductoDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductoDbApplication.class, args);
    }

}
