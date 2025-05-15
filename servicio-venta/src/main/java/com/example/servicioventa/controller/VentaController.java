package com.example.servicioventa.controller;

import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    public Optional<Venta> obtenerVentaPorId(@PathVariable Integer id) {
        return ventaService.listarPorId(id);
    }

    @PostMapping
    public Venta crearVenta(@RequestBody Venta venta) {
        return ventaService.guardar(venta);
    }

    @PutMapping("/{id}")
    public Venta actualizarVenta(@PathVariable Integer id, @RequestBody Venta venta) {
        venta.setId(id);  // Aseguramos que la actualización respete el ID enviado
        return ventaService.actualizar(venta);
    }

    @DeleteMapping("/{id}")
    public void eliminarVenta(@PathVariable Integer id) {
        ventaService.eliminarPorId(id);
    }
}