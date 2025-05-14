package com.example.servicioventa.util;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.repository.VentaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class VentaSeeder {

    private final VentaRepository ventaRepository;

    public VentaSeeder(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @PostConstruct
    public void seed() {
        if (ventaRepository.count() == 0) {  // Solo ejecuta si no hay datos
            List<Venta> ventas = List.of(
                    new Venta(null, 1, LocalDateTime.now(), new BigDecimal("150.50"), "COMPLETADO"),
                    new Venta(null, 2, LocalDateTime.now(), new BigDecimal("250.00"), "PENDIENTE"),
                    new Venta(null, 3, LocalDateTime.now(), new BigDecimal("320.75"), "CANCELADO"),
                    new Venta(null, 4, LocalDateTime.now(), new BigDecimal("90.30"), "COMPLETADO"),
                    new Venta(null, 5, LocalDateTime.now(), new BigDecimal("180.00"), "PENDIENTE")
            );
            ventaRepository.saveAll(ventas);
        }
    }
}