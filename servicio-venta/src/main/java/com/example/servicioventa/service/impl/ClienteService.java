package com.example.servicioventa.service.impl;

import com.example.servicioventa.dto.Cliente;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "clienteCircuitBreaker", fallbackMethod = "fallbackObtenerClientePorId")
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

        throw new RuntimeException("No se encontraron instancias de servicio-cliente en Eureka.");
    }

    // Metodo Fallback cuando el servicio no está disponible
    public Cliente fallbackObtenerClientePorId(Integer cliente_id, Throwable t) {
        System.out.println("El servicio cliente no está disponible. Devolviendo datos vacíos.");

        Cliente cliente = new Cliente();
        cliente.setId(cliente_id);
        cliente.setNombre("Información no disponible");
        cliente.setDireccion("Información no disponible");
        return cliente;
    }

    // Metodo necesario para el seeder
    public List<Integer> getAllClienteIds() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("servicio-cliente");

        if (serviceInstance != null) {
            String baseUrl = serviceInstance.getUri().toString();
            String url = baseUrl + "/clientes";

            try {
                Cliente[] clientes = restTemplate.getForObject(url, Cliente[].class);
                if (clientes != null) {
                    return Arrays.stream(clientes)
                            .map(Cliente::getId)
                            .collect(Collectors.toList());
                } else {
                    return Collections.emptyList();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error al obtener la lista de clientes: " + e.getMessage());
            }
        }

        throw new RuntimeException("No se encontraron instancias del servicio servicio-cliente.");
    }
}
