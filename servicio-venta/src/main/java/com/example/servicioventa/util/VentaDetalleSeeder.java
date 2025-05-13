package com.example.servicioventa.util;

import com.example.servicioventa.entity.VentaDetalle;
import com.example.servicioventa.repository.VentaDetalleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class VentaDetalleSeeder {

    private final VentaDetalleRepository ventaDetalleRepository;

    public VentaDetalleSeeder(VentaDetalleRepository ventaDetalleRepository) {
        this.ventaDetalleRepository = ventaDetalleRepository;
    }

    @PostConstruct
    public void seed() {
        if (ventaDetalleRepository.count() == 0) {  // Solo ejecuta si no hay datos
            List<VentaDetalle> detalles = List.of(
                    new VentaDetalle(null, 1, 101, 2, new BigDecimal("75.25"), new BigDecimal("150.50")),
                    new VentaDetalle(null, 2, 102, 5, new BigDecimal("50.00"), new BigDecimal("250.00")),
                    new VentaDetalle(null, 3, 103, 3, new BigDecimal("107.00"), new BigDecimal("321.00")),
                    new VentaDetalle(null, 4, 104, 1, new BigDecimal("90.30"), new BigDecimal("90.30")),
                    new VentaDetalle(null, 5, 105, 2, new BigDecimal("90.00"), new BigDecimal("180.00"))
            );
            ventaDetalleRepository.saveAll(detalles);
        }
    }
}