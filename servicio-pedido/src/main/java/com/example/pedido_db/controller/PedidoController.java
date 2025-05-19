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
    public ResponseEntity<?> save(@RequestBody Pedido pedido) {
        try {
            Pedido pedidoGuardado = pedidoService.guardar(pedido);
            return ResponseEntity.ok(pedidoGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // Actualizar un pedido existente
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> update(@PathVariable Integer id, @RequestBody Pedido pedido) {
        pedido.setId(id);  // asegura que el pedido tenga
        Pedido pedidoActualizado = pedidoService.actualizar(pedido);
        return ResponseEntity.ok(pedidoActualizado);
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