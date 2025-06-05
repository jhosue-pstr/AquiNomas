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
public class DetallePedidoServiceimpl {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    private String getBaseUrl() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("servicio-pedido");
        if (serviceInstance != null) {
            return serviceInstance.getUri().toString();
        }
        throw new RuntimeException("No se encontraron instancias del servicio-pedido en Eureka.");
    }

    public Pedido obtenerPedidoPorId(Integer pedidoId) {
        String url = getBaseUrl() + "/pedidos/" + pedidoId;
        try {
            return restTemplate.getForObject(url, Pedido.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener pedido con ID " + pedidoId + ": " + e.getMessage());
        }
    }

    public Detalle_pedido obtenerDetallePedidoPorId(Integer detalleId) {
        String url = getBaseUrl() + "/detalle_pedido/" + detalleId;
        try {
            return restTemplate.getForObject(url, Detalle_pedido.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener detalle pedido con ID " + detalleId + ": " + e.getMessage());
        }
    }

    public List<Detalle_pedido> obtenerDetallesPorPedido(Integer pedidoId) {
        String url = getBaseUrl() + "/pedidos/" + pedidoId + "/detalle_pedido";
        try {
            Detalle_pedido[] detalles = restTemplate.getForObject(url, Detalle_pedido[].class);
            return detalles != null ? Arrays.asList(detalles) : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener detalles de pedido ID " + pedidoId + ": " + e.getMessage());
        }
    }
}
