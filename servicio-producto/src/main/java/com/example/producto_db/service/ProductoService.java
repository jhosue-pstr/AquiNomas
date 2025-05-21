package com.example.producto_db.service;

import com.example.producto_db.entity.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> listar();


    List<Producto> listarProductosDisponibles();

    Producto guardar(Producto producto);
    Optional<Producto> listarPorId(Integer id);
    Producto actualizar(Producto producto);
    void eliminar(Integer id);
}
