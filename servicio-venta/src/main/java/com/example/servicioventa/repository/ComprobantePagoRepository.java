package com.example.servicioventa.repository;

import com.example.servicioventa.entity.Comprobante_pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComprobantePagoRepository extends JpaRepository<Comprobante_pago, Integer> {
    Optional<Comprobante_pago> findById(Integer id);
}
