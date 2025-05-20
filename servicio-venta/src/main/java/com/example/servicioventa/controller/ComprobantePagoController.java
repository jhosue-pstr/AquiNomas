package com.example.servicioventa.controller;

import com.example.servicioventa.entity.Comprobante_pago;
import com.example.servicioventa.service.ComprobantePagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/comprobantes")
public class ComprobantePagoController {

    @Autowired
    private ComprobantePagoService comprobantePagoService;

    @GetMapping
    public ResponseEntity<List<Comprobante_pago>> listarComprobantes() {
        return ResponseEntity.ok(comprobantePagoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comprobante_pago> obtenerComprobantePorId(@PathVariable Integer id) {
        return comprobantePagoService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comprobante no encontrado"));
    }

    @PostMapping
    public ResponseEntity<Comprobante_pago> crearComprobante(@RequestBody Comprobante_pago comprobantePago) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comprobantePagoService.guardar(comprobantePago));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comprobante_pago> actualizarComprobante(@PathVariable Integer id, @RequestBody Comprobante_pago comprobantePago) {
        comprobantePago.setId(id);
        return ResponseEntity.ok(comprobantePagoService.actualizar(comprobantePago));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Comprobante_pago> actualizarComprobante(@PathVariable Integer id, @RequestBody Comprobante_pago comprobantePago) {
//        if (!comprobantePagoService.listarPorId(id).isPresent()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comprobante no encontrado con ID: " + id);
//        }
//        return ResponseEntity.ok(comprobantePagoService.actualizar(comprobantePago));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComprobante(@PathVariable Integer id) {
        comprobantePagoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
