package com.example.servicioventa.service.impl;

import com.example.servicioventa.dto.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    public Producto obtenerProductoPorId(Integer producto_id) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("servicio-producto");

        if (serviceInstance != null) {
            String baseUrl = serviceInstance.getUri().toString();
            String url = baseUrl + "/productos/" + producto_id;

            try {
                return restTemplate.getForObject(url, Producto.class);
            } catch (Exception e) {
                throw new RuntimeException("Error al obtener producto con ID " + producto_id + ": " + e.getMessage());
            }
        }

        throw new RuntimeException("No se encontraron instancias del servicio servicio-producto en Eureka.");
    }

    public Optional<Object> listarPorId(Integer productoId) {
        return null;
    }
}
