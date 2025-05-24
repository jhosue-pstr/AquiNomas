package com.example.servicioventa.service.impl;

import com.example.servicioventa.entity.Detalle_Venta;
import com.example.servicioventa.repository.VentaDetalleRepository;
import com.example.servicioventa.service.VentaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VentaDetalleServiceImpl implements VentaDetalleService {

    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;

    @Override
    public List<Detalle_Venta> listar() {
        return ventaDetalleRepository.findAll();
    }

    @Override
    public Detalle_Venta guardar(Detalle_Venta detalleVenta) {
        if (detalleVenta.getCantidad() == null || detalleVenta.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        if (detalleVenta.getPrecio_unitario() == null || detalleVenta.getPrecio_unitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero.");
        }

        // Calcular el total antes de guardar
        detalleVenta.setTotal(detalleVenta.getPrecio_unitario().multiply(new BigDecimal(detalleVenta.getCantidad())));
        return ventaDetalleRepository.save(detalleVenta);
    }

    @Override
    public Detalle_Venta actualizar(Detalle_Venta detalleVenta) {
        if (!ventaDetalleRepository.existsById(detalleVenta.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se puede actualizar. Detalle_Venta no encontrado con ID: " + detalleVenta.getId());
        }
        return ventaDetalleRepository.save(detalleVenta);
    }

    @Override
    public Optional<Detalle_Venta> listarPorId(Integer id) {
        return ventaDetalleRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!ventaDetalleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VentaDetalle no encontrado con ID: " + id);
        }
        ventaDetalleRepository.deleteById(id);
    }

}
