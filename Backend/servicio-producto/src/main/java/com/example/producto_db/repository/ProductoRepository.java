package com.example.producto_db.repository;

import com.example.producto_db.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByIdIn(List<Integer> ids);

}
