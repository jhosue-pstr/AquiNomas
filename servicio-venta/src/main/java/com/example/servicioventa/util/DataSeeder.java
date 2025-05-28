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
import java.util.ArrayList;
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
    public void run(String... args) {
        System.out.println("🔄 Iniciando Seeder...");

        if (ventaRepository.count() < 5) {
            List<Venta> ventas = seedVentas(); // Generar nuevas ventas hasta completar 5
            seedVentaDetalles(ventas); // Luego detalles de venta
            seedComprobantes(ventas); // Finalmente comprobantes
        } else {
            System.out.println("⚠️ Ya hay suficientes ventas, no se generarán nuevos registros.");
        }
    }

    private List<Venta> seedVentas() {
        long ventasExistentes = ventaRepository.count();
        int ventasFaltantes = (int) (5 - ventasExistentes);
        List<Venta> nuevasVentas = new ArrayList<>();

        for (int i = 1; i <= ventasFaltantes; i++) {
            Venta venta = new Venta(null, i, List.of(), LocalDateTime.now(),
                    new BigDecimal((i + 1) * 100), "Pendiente");
            nuevasVentas.add(venta);
        }

        List<Venta> ventasGuardadas = ventaRepository.saveAll(nuevasVentas);
        System.out.println("✅ Ventas creadas dinámicamente: " + ventasGuardadas.size());

        return ventasGuardadas;
    }

    private void seedVentaDetalles(List<Venta> ventas) {
        long detallesExistentes = ventaDetalleRepository.count();
        int detallesFaltantes = (int) (5 - detallesExistentes);

        if (!ventas.isEmpty() && detallesFaltantes > 0) {
            List<Detalle_Venta> nuevosDetalles = new ArrayList<>();

            for (int i = 0; i < detallesFaltantes; i++) {
                Detalle_Venta detalle = new Detalle_Venta(null, ventas.get(i % ventas.size()),
                        i + 1, 2 + i, new BigDecimal("50.00"), new BigDecimal("100.00"));
                nuevosDetalles.add(detalle);
            }

            ventaDetalleRepository.saveAll(nuevosDetalles);
            System.out.println("✅ Detalles de venta creados dinámicamente: " + nuevosDetalles.size());
        } else {
            System.out.println("⚠️ Ya hay suficientes detalles de venta.");
        }
    }

    private void seedComprobantes(List<Venta> ventas) {
        long comprobantesExistentes = comprobantePagoRepository.count();
        int comprobantesFaltantes = (int) (5 - comprobantesExistentes);

        if (!ventas.isEmpty() && comprobantesFaltantes > 0) {
            List<Comprobante_pago> nuevosComprobantes = new ArrayList<>();

            for (int i = 0; i < comprobantesFaltantes; i++) {
                Venta venta = ventas.get(i % ventas.size());

                BigDecimal igvVenta = venta.getTotal().multiply(BigDecimal.valueOf(0.18));

                Comprobante_pago comprobante = new Comprobante_pago(null, venta, "Factura", "XYZ" + i,
                        "002-00" + i, LocalDateTime.now(), null, "Cliente " + (i + 1),
                        "1234567890" + i, "Dirección " + (i + 1), venta.getTotal(),
                        igvVenta, venta.getTotal().subtract(igvVenta), "Efectivo", "Pagado");

                nuevosComprobantes.add(comprobante);
            }

            comprobantePagoRepository.saveAll(nuevosComprobantes);
            System.out.println("✅ Comprobantes creados dinámicamente: " + nuevosComprobantes.size());
        } else {
            System.out.println("⚠️ Ya hay suficientes comprobantes.");
        }
    }
}