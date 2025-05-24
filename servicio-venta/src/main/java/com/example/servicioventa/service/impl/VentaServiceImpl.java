package com.example.servicioventa.service.impl;

import com.example.servicioventa.dto.Pedido;
import com.example.servicioventa.entity.Detalle_Venta;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.repository.VentaDetalleRepository;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaDetalleRepository detalleVentaRepository;

    @Autowired
    private PedidoServiceImpl pedidoService; // Servicio para obtener pedidos dinámicamente

    @Override
    public Venta guardarVenta(Venta venta, List<Detalle_Venta> detallesVenta) {
        for (Detalle_Venta detalle : detallesVenta) {
            Pedido pedido = pedidoService.obtenerPedidoPorId(detalle.getPedido_id());

            if (pedido == null) {
                throw new IllegalArgumentException("Pedido con ID " + detalle.getPedido_id() + " no encontrado.");
            }

            if ("pendiente".equalsIgnoreCase(pedido.getEstado())) {
                throw new IllegalStateException("No se puede realizar la venta, el pedido aún está pendiente.");
            }

            if (!pedido.getCliente_id().equals(venta.getCliente_id())) {
                throw new IllegalArgumentException("El cliente del pedido no coincide con el cliente de la venta.");
            }
        }

        // Si pasa todas las validaciones, guardamos la venta y sus detalles
        Venta ventaGuardada = ventaRepository.save(venta);
        detallesVenta.forEach(detalle -> detalle.setVenta(ventaGuardada));
        detalleVentaRepository.saveAll(detallesVenta);

        return ventaGuardada;
    }


    @Override
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta actualizar(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> listarPorId(Integer id) {
        return ventaRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        ventaRepository.deleteById(id);
    }
}
