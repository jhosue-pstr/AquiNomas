package com.example.producto_db.service.impl;

import com.example.producto_db.entity.Producto;
import com.example.producto_db.repository.ProductoRepository;
import com.example.producto_db.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> listarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto actualizar(Producto producto) {
        Producto productoDB = productoRepository.findById(producto.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + producto.getId()));

        productoDB.setNombre(producto.getNombre());
        productoDB.setCategoria(producto.getCategoria());
        productoDB.setDescripcion(producto.getDescripcion());
        productoDB.setPrecio(producto.getPrecio());
        productoDB.setStock(producto.getStock());
        productoDB.setStockMinimo(producto.getStockMinimo()); // ✅ nuevo campo añadido

        return productoRepository.save(productoDB);
    }


    @Override
    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }
}
