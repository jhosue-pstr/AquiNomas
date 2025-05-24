package com.example.servicioventa.controller;

import com.example.servicioventa.entity.Detalle_Venta;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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

//    @GetMapping("/{id}")
//    public Optional<Venta> obtenerVentaPorId(@PathVariable Integer id) {
//        return ventaService.listarPorId(id);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Integer id) {
        return ventaService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
    }

    @PostMapping
    public ResponseEntity<Venta> crearVenta(@RequestBody Venta venta, @RequestBody List<Detalle_Venta> detallesVenta) {
        if (venta.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El total debe ser mayor a cero.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.guardarVenta(venta, detallesVenta));
    }


    @PutMapping("/{id}")
    public Venta actualizarVenta(@PathVariable Integer id, @RequestBody Venta venta) {
        venta.setId(id);  // Aseguramos que la actualización respete el ID enviado
        return ventaService.actualizar(venta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Integer id) {
        ventaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

}