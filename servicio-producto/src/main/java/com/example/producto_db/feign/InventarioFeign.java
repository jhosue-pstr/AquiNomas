package com.example.producto_db.feign;

import com.example.producto_db.dto.Inventario;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "servicio-inventario")
public interface InventarioFeign {

    @GetMapping("/inventario")
    List<Inventario> listarInventario();
}
