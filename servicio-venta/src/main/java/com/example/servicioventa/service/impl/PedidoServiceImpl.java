package com.example.servicioventa.service.impl;

import com.example.servicioventa.dto.Detalle_pedido;
import com.example.servicioventa.dto.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PedidoServiceImpl {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    public Pedido obtenerPedidoPorId(Integer pedido_id) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("servicio-pedido");

        if (serviceInstance != null) {
            String baseUrl = serviceInstance.getUri().toString();
            String url = baseUrl + "/pedidos/" + pedido_id;

            try {
                return restTemplate.getForObject(url, Pedido.class);
            } catch (Exception e) {
                throw new RuntimeException("Error al obtener pedido con ID " + pedido_id + ": " + e.getMessage());
            }
        }

        throw new RuntimeException("No se encontraron instancias del servicio servicio-pedido en Eureka.");
    }

    public List<Detalle_pedido> obtenerDetallesPorPedidoId(Integer pedido_id) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("servicio-pedido");

        if (serviceInstance != null) {
            String baseUrl = serviceInstance.getUri().toString();
            String url = baseUrl + "/pedidos/" + pedido_id + "/detalles";

            try {
                Detalle_pedido[] detalles = restTemplate.getForObject(url, Detalle_pedido[].class);
                return detalles != null ? Arrays.asList(detalles) : Collections.emptyList();
            } catch (Exception e) {
                throw new RuntimeException("Error al obtener detalles del pedido con ID " + pedido_id + ": " + e.getMessage());
            }
        }

        throw new RuntimeException("No se encontraron instancias del servicio servicio-pedido en Eureka.");
    }
}

