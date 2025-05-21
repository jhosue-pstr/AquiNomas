package com.example.producto_db.service.impl;

import com.example.producto_db.dto.Inventario;
import com.example.producto_db.dto.ProductoDisponibleDTO;
import com.example.producto_db.entity.Producto;
import com.example.producto_db.feign.InventarioFeign;
import com.example.producto_db.repository.ProductoRepository;
import com.example.producto_db.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    @Autowired
    private InventarioFeign inventarioFeign;

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
    public List<ProductoDisponibleDTO> listarProductosDisponibles() {
        List<Inventario> inventarios = inventarioFeign.listarInventario();

        // Filtrar inventarios con cantidad disponible >= 1
        List<Inventario> inventariosConStock = inventarios.stream()
                .filter(inv -> inv.getCantidadDisponible() != null && inv.getCantidadDisponible() >= 1)
                .collect(Collectors.toList());

        // Obtener lista de IDs de productos con stock
        List<Integer> productosConStockIds = inventariosConStock.stream()
                .map(Inventario::getProductoId)
                .collect(Collectors.toList());

        // Obtener productos que tienen stock
        List<Producto> productos = productoRepository.findByIdIn(productosConStockIds);

        // Mapear productos a DTOs incluyendo cantidad disponible
        return productos.stream()
                .map(prod -> {
                    Inventario inv = inventariosConStock.stream()
                            .filter(i -> i.getProductoId().equals(prod.getId()))
                            .findFirst()
                            .orElse(null);

                    int cantidad = (inv != null && inv.getCantidadDisponible() != null) ? inv.getCantidadDisponible() : 0;

                    return new ProductoDisponibleDTO(prod, cantidad);
                })
                .collect(Collectors.toList());
    }



    @Override
    public Producto actualizar(Producto producto) {
        Producto productoDB = productoRepository.findById(producto.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + producto.getId()));

        productoDB.setNombre(producto.getNombre());
        productoDB.setCategoria(producto.getCategoria());
        productoDB.setDescripcion(producto.getDescripcion());
        productoDB.setPrecio(producto.getPrecio());


        return productoRepository.save(productoDB);
    }














    @Override
    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }
}
