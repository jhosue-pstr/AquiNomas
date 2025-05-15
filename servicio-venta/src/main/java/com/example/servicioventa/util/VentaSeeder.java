package com.example.servicioventa.util;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.impl.ClienteService;
import com.example.servicioventa.service.impl.ProductoService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class VentaSeeder {

    private final VentaRepository ventaRepository;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final VentaDetalleSeeder ventaDetalleSeeder;

    public VentaSeeder(VentaRepository ventaRepository, ClienteService clienteService, ProductoService productoService, VentaDetalleSeeder ventaDetalleSeeder) {
        this.ventaRepository = ventaRepository;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.ventaDetalleSeeder = ventaDetalleSeeder;
    }

    @PostConstruct
    public void init() {
        try {
            if (ventaRepository.count() == 0) {  // Verifica si ya existen ventas
                List<Integer> clienteIds = clienteService.getAllClienteIds();  // Obtener todos los IDs de clientes

                if (!clienteIds.isEmpty()) {
                    for (int i = 0; i < 5; i++) {  // Crear 5 ventas de ejemplo
                        Integer clienteId = clienteIds.get(new Random().nextInt(clienteIds.size()));  // Selección aleatoria de un cliente
                        BigDecimal total = new BigDecimal(100 + new Random().nextInt(500));  // Total aleatorio entre 100 y 600
                        String estado = new Random().nextBoolean() ? "COMPLETADO" : "PENDIENTE";  // Estado aleatorio

                        Venta venta = new Venta(
                                null,  // ID autogenerado
                                clienteId,
                                LocalDateTime.now(),
                                total,
                                estado
                        );

                        // Guardar la venta
                        Venta savedVenta = ventaRepository.save(venta);

                        // Sembrar detalles de venta para la venta guardada
                        ventaDetalleSeeder.seedVentaDetalles(savedVenta.getId());
                    }
                    System.out.println("Seeder ejecutado: ventas inicializadas correctamente.");
                } else {
                    System.out.println("No se encontraron clientes, no se crearon ventas.");
                }
            } else {
                System.out.println("Las ventas ya están sembradas, no se insertaron nuevos datos.");
            }
        } catch (Exception e) {
            System.err.println("Error al ejecutar el Seeder de Ventas: " + e.getMessage());
        }
    }
}