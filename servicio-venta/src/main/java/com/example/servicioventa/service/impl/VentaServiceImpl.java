package com.example.servicioventa.service.impl;

import com.example.servicioventa.dto.Pedido;
import com.example.servicioventa.entity.Detalle_Venta;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.repository.VentaDetalleRepository;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.VentaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
   //@Transactional
   public Venta guardarVenta(Venta venta, List<Detalle_Venta> detallesVenta) {
       BigDecimal totalVenta = BigDecimal.ZERO;

       for (Detalle_Venta detalle : detallesVenta) {
           Pedido pedido = pedidoService.obtenerPedidoPorId(detalle.getPedido_id());

           if (pedido == null) {
               throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido con ID " + detalle.getPedido_id() + " no encontrado.");
           }

           if ("pendiente".equalsIgnoreCase(pedido.getEstado())) {
               throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede realizar la venta, el pedido aún está pendiente.");
           }

           if (!pedido.getCliente_id().equals(venta.getCliente_id())) {
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente del pedido no coincide con el cliente de la venta.");
           }

           // Calcular total de cada detalle de venta
           detalle.setTotal(detalle.getPrecio_unitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));

           // Acumular el total de la venta
           totalVenta = totalVenta.add(detalle.getTotal());
       }

       // Validación del total de la venta antes de guardar
       if (totalVenta.compareTo(BigDecimal.ZERO) <= 0) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El total de la venta debe ser mayor a cero.");
       }

       venta.setTotal(totalVenta);
       Venta ventaGuardada = ventaRepository.save(venta);

       detallesVenta.forEach(detalle -> detalle.setVenta(ventaGuardada));
       detalleVentaRepository.saveAll(detallesVenta);

       return ventaGuardada;
   }

    @Override
    public List<Venta> listar() {
        List<Venta> ventas = ventaRepository.findAll();
        System.out.println("🔍 Ventas encontradas: " + ventas.size());
        return ventas;
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
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada con ID: " + id));
        ventaRepository.delete(venta);
    }
}
