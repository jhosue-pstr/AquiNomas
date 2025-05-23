package com.upeu.serviciocompra.controller;

import com.upeu.serviciocompra.dto.CompraDto;
import com.upeu.serviciocompra.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    // Crear compra - retorna CompraDto con detalles y proveedor
    @PostMapping("/solicitud")
    public CompraDto crearCompra(@RequestBody CompraDto compraDto) {
        return compraService.crearCompra(compraDto);
    }

    // Obtener todas las compras - lista de CompraDto
    @GetMapping
    public List<CompraDto> obtenerTodasLasCompras() {
        return compraService.obtenerTodasLasCompras();
    }

    // Obtener compra por id - retorna CompraDto
    @GetMapping("/{id}")
    public CompraDto obtenerCompraPorId(@PathVariable Integer id) {
        return compraService.obtenerCompraPorId(id);
    }

    // Actualizar compra - retorna CompraDto actualizado
    @PutMapping("/{id}")
    public CompraDto actualizarCompra(@PathVariable Integer id, @RequestBody CompraDto compraDto) {
        return compraService.actualizarCompra(id, compraDto);
    }

    // Eliminar compra - void
    @DeleteMapping("/{id}")
    public void eliminarCompra(@PathVariable Integer id) {
        compraService.eliminarCompra(id);
    }

    // Validar compra - retorna CompraDto o null
    @PutMapping("/{id}/validar")
    public CompraDto validarCompra(@PathVariable Integer id) {
        return compraService.validarCompra(id);
    }

    // Generar orden compra - retorna CompraDto
    @GetMapping("/{id}/orden")
    public CompraDto generarOrdenCompra(@PathVariable Integer id) {
        return compraService.generarOrdenCompra(id);
    }
}
