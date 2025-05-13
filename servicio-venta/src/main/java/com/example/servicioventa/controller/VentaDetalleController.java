package com.example.servicioventa.controller;

import com.example.servicioventa.entity.VentaDetalle;
import com.example.servicioventa.service.VentaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventasdetalles")
public class VentaDetalleController {

    @Autowired
    private VentaDetalleService ventaDetalleService;

    @GetMapping
    public List<VentaDetalle> listarDetalles() {
        return ventaDetalleService.listar();
    }

    @GetMapping("/{id}")
    public Optional<VentaDetalle> obtenerDetallePorId(@PathVariable Integer id) {
        return ventaDetalleService.listarPorId(id);
    }

    @PostMapping
    public VentaDetalle crearDetalle(@RequestBody VentaDetalle ventaDetalle) {
        return ventaDetalleService.guardar(ventaDetalle);
    }
0
    @PutMapping("/{id}")
    public VentaDetalle actualizarDetalle(@PathVariable Integer id, @RequestBody VentaDetalle ventaDetalle) {
        ventaDetalle.setId(id);  // Asignar el ID antes de actualizar
        return ventaDetalleService.actualizar(ventaDetalle);
    }

    @DeleteMapping("/{id}")
    public void eliminarDetalle(@PathVariable Integer id) {
        ventaDetalleService.eliminarPorId(id);
    }
}