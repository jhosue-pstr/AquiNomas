package com.example.servicioventa.repository;

import com.example.servicioventa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import com.example.servicioventa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    Optional<Venta> findById(Integer id);
}
