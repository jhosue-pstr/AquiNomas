package com.upeu.serviciocompra.repository;

import com.upeu.serviciocompra.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {  // ID de Compra como Integer
}
