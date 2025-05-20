package com.example.servicioventa.util;

import com.example.servicioventa.entity.Comprobante_pago;
import com.example.servicioventa.entity.Detalle_Venta;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.repository.ComprobantePagoRepository;
import com.example.servicioventa.repository.VentaDetalleRepository;
import com.example.servicioventa.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;

    @Autowired
    private ComprobantePagoRepository comprobantePagoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (ventaRepository.count() == 0) {
            List<Venta> ventas = seedVentas();
            seedVentaDetalles(ventas);
        }

        if (comprobantePagoRepository.count() == 0) {
            seedComprobantes();
        }
    }

    private List<Venta> seedVentas() {
        Venta venta1 = new Venta(null, 1, LocalDateTime.now(), new BigDecimal("250.00"), "Pagado");
        Venta venta2 = new Venta(null, 2, LocalDateTime.now(), new BigDecimal("500.00"), "Pendiente");

        List<Venta> ventasGuardadas = ventaRepository.saveAll(List.of(venta1, venta2));
        System.out.println("✅ Seeded Ventas");

        return ventasGuardadas; // Devuelve las ventas guardadas con sus IDs generados
    }

    private void seedVentaDetalles(List<Venta> ventas) {
        if (!ventas.isEmpty()) {
            Detalle_Venta detalle1 = new Detalle_Venta(null, ventas.get(0), 101, 2, new BigDecimal("50.00"), new BigDecimal("100.00"));
            Detalle_Venta detalle2 = new Detalle_Venta(null, ventas.get(1), 102, 3, new BigDecimal("70.00"), new BigDecimal("210.00"));

            ventaDetalleRepository.saveAll(List.of(detalle1, detalle2));
            System.out.println("✅ Seeded VentaDetalles");
        } else {
            System.out.println("⚠️ No se insertaron detalles porque no hay ventas.");
        }
    }

    private void seedComprobantes() {
        Comprobante_pago comprobante1 = new Comprobante_pago(null, "Factura", "ABC123", "001-0001", LocalDateTime.now(), 1, "Juan Pérez", "12345678901", "Av. Central 123", new BigDecimal("250.00"), new BigDecimal("45.00"), new BigDecimal("205.00"), "Pagado");
        Comprobante_pago comprobante2 = new Comprobante_pago(null, "Boleta", "XYZ456", "002-0002", LocalDateTime.now(), 2, "María López", null, null, new BigDecimal("500.00"), null, new BigDecimal("500.00"), "Pendiente");

        comprobantePagoRepository.saveAll(List.of(comprobante1, comprobante2));
        System.out.println("✅ Seeded Comprobantes");
    }
}
