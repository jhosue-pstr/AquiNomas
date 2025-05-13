package com.upeu.serviciocompra.controller;

import com.upeu.serviciocompra.dto.CompraDTO;
import com.upeu.serviciocompra.entity.Compra;
import com.upeu.serviciocompra.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    @GetMapping
    public ResponseEntity<List<CompraDTO>> listarCompras() {
        return ResponseEntity.ok(compraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> obtenerCompra(@PathVariable Integer id) {
        return ResponseEntity.ok(compraService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CompraDTO> crearCompra(@RequestBody Compra compra) {
        return ResponseEntity.ok(compraService.guardar(compra));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Integer id) {
        compraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
