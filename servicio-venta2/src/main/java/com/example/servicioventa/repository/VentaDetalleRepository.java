package com.example.servicioventa.repository;

import com.example.servicioventa.entity.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Integer> {
    Optional<VentaDetalle> findById(Integer id);

    void deleteById(Long aLong);
}
