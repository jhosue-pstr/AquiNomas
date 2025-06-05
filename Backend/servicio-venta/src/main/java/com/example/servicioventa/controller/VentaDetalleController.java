package com.example.servicioventa.controller;

import com.example.servicioventa.entity.Detalle_Venta;
import com.example.servicioventa.service.VentaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventasdetalle")
public class VentaDetalleController {

    @Autowired
    private VentaDetalleService ventaDetalleService;

    @GetMapping
    public List<Detalle_Venta> listarDetalles() {
        return ventaDetalleService.listar();
    }

//    @GetMapping("/{id}")
//    public Optional<Detalle_Venta> obtenerDetallePorId(@PathVariable Integer id) {
//        return ventaDetalleService.listarPorId(id);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Detalle_Venta> obtenerDetallePorId(@PathVariable Integer id) {
        return ventaDetalleService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de venta no encontrado"));
    }

//    @PostMapping
//    public Detalle_Venta crearDetalle(@RequestBody Detalle_Venta detalleVenta) {
//        return ventaDetalleService.guardar(detalleVenta);
//    }

    @PostMapping
    public ResponseEntity<Detalle_Venta> crearDetalle(@RequestBody Detalle_Venta detalleVenta) {
        if (detalleVenta.getCantidad() <= 0 || detalleVenta.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cantidad y total deben ser mayores a cero.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaDetalleService.guardar(detalleVenta));
    }

    @PutMapping("/{id}")
    public Detalle_Venta actualizarDetalle(@PathVariable Integer id, @RequestBody Detalle_Venta detalleVenta) {
        detalleVenta.setId(id);  // Asignar el ID antes de actualizar
        return ventaDetalleService.actualizar(detalleVenta);
    }

    @DeleteMapping("/{id}")
    public void eliminarDetalle(@PathVariable Integer id) {
        ventaDetalleService.eliminarPorId(id);
    }
}