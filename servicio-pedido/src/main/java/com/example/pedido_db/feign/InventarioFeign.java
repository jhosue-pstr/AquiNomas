package com.example.pedido_db.feign;

import com.example.pedido_db.dto.Inventario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "servicio-inventario", url = "http://localhost:5243")
public interface InventarioFeign {

    @GetMapping("/inventario")
    List<Inventario> obtenerInventarios();


}
