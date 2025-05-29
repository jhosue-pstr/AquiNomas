package com.example.pedido_db.feign;

import com.example.pedido_db.dto.Inventario;
import com.example.pedido_db.dto.InventarioUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "servicio-inventario")
public interface InventarioFeign {

    @GetMapping("/inventario")
    List<Inventario> obtenerInventarios();

    @PutMapping("/inventario/{id}")
    ResponseEntity<Void> actualizarInventario(@PathVariable("id") Integer inventarioId,
                                              @RequestBody InventarioUpdateRequest updateRequest);

}

