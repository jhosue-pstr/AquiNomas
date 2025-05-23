package com.upeu.serviciocompra.repository;

import com.upeu.serviciocompra.entity.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {
    List<DetalleCompra> findByCompraId(Integer compraId);
}
