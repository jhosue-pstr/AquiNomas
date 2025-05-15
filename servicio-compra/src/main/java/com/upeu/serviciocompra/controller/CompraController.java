package com.upeu.serviciocompra.controller;

import com.upeu.serviciocompra.dto.CompraDto;
import com.upeu.serviciocompra.entity.Compra;
import com.upeu.serviciocompra.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping
    public Compra crearCompra(@RequestBody CompraDto compraDto) {
        return compraService.crearCompra(compraDto);
    }

    @GetMapping
    public List<Compra> obtenerTodasLasCompras() {
        return compraService.obtenerTodasLasCompras();
    }

    @GetMapping("/{id}")
    public Compra obtenerCompraPorId(@PathVariable Integer id) {
        return compraService.obtenerCompraPorId(id);
    }

    @PutMapping("/{id}")
    public Compra actualizarCompra(@PathVariable Integer id, @RequestBody CompraDto compraDto) {
        return compraService.actualizarCompra(id, compraDto);
    }

    @DeleteMapping("/{id}")
    public void eliminarCompra(@PathVariable Integer id) {
        compraService.eliminarCompra(id);
    }
}
