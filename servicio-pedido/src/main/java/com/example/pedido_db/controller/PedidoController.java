package com.example.pedido_db.controller;

import com.example.pedido_db.entity.Pedido;
import com.example.pedido_db.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> list() {
        List<Pedido> pedidos = pedidoService.listar();
        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidos);
    }
    // Guardar un nuevo pedido
    @PostMapping
    public ResponseEntity<Pedido> save(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.guardar(pedido));
    }

    // Actualizar un pedido existente
    @PutMapping
    public ResponseEntity<Pedido> update(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.actualizar(pedido));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> listById(@PathVariable Integer id) {
        Optional<Pedido> pedido = pedidoService.listarPorId(id);
        if (pedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido.get());
    }

    // Eliminar un pedido por ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        pedidoService.eliminar(id);
    }


}