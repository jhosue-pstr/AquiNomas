package com.example.servicioventa.service.impl;

import com.example.servicioventa.dto.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClienteService {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    public Cliente obtenerClientePorId(Integer cliente_id) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("servicio-cliente");

        if (serviceInstance != null) {
            String baseUrl = serviceInstance.getUri().toString();
            String url = baseUrl + "/clientes/" + cliente_id;

            try {
                return restTemplate.getForObject(url, Cliente.class);
            } catch (Exception e) {
                throw new RuntimeException("Error al obtener cliente con ID " + cliente_id + ": " + e.getMessage());
            }
        }

        throw new RuntimeException("No se encontraron instancias del servicio servicio-cliente en Eureka.");
    }
}
