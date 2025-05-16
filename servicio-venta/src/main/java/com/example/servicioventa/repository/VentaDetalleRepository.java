package com.example.servicioventa.repository;

import com.example.servicioventa.entity.Detalle_Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VentaDetalleRepository extends JpaRepository<Detalle_Venta, Integer> {
    Optional<Detalle_Venta> findById(Integer id);

    void deleteById(Long aLong);
}
