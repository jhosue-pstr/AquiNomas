package com.example.servicioventa.util;

import com.example.servicioventa.entity.Detalle_Venta;
import com.example.servicioventa.repository.VentaDetalleRepository;
import com.example.servicioventa.service.impl.ProductoService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Component
public class VentaDetalleSeeder {

    private final VentaDetalleRepository ventaDetalleRepository;
    private final ProductoService productoService;

    public VentaDetalleSeeder(VentaDetalleRepository ventaDetalleRepository, ProductoService productoService) {
        this.ventaDetalleRepository = ventaDetalleRepository;
        this.productoService = productoService;
    }

    // Este metodo será invocado desde el VentaSeeder por cada venta creada
    public void seedVentaDetalles(Integer ventaId) {
        if (ventaId == null) return;

        List<Integer> productoIds = productoService.getAllProductoIds();
        if (productoIds.isEmpty()) return;

        Random random = new Random();
        for (int i = 0; i < 2; i++) {  // 2 detalles por venta
            Integer productoId = productoIds.get(random.nextInt(productoIds.size()));
            int cantidad = random.nextInt(3) + 1;  // 1 a 3 unidades
            BigDecimal precioUnitario = new BigDecimal(10 + random.nextInt(90)); // 10.00 a 100.00
            BigDecimal total = precioUnitario.multiply(new BigDecimal(cantidad));

            Detalle_Venta detalle = new Detalle_Venta(
                    null,
                    ventaId,
                    productoId,
                    cantidad,
                    precioUnitario,
                    total
            );

            ventaDetalleRepository.save(detalle);
        }
    }
}