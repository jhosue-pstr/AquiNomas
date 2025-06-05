package com.example.producto_db.controller;

import com.example.producto_db.dto.ProductoDisponibleDTO;
import com.example.producto_db.entity.Producto;
import com.example.producto_db.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable Integer id) {
        return productoService.listarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @GetMapping("/producto-disponible")
    public List<ProductoDisponibleDTO> listarProductosDisponibles() {
        return productoService.listarProductosDisponibles();
    }



    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    @PutMapping("/{id}")  // Usar {id} en la ruta
    public Producto actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        // Llamar al servicio para actualizar el producto con el ID
        producto.setId(id);  // Asegúrate de que el ID del producto coincida
        return productoService.actualizar(producto);
    }


    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
    }
}
