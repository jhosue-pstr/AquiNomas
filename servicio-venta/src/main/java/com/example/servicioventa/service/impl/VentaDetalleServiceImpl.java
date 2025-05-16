package com.example.servicioventa.service.impl;

import com.example.servicioventa.entity.Detalle_Venta;
import com.example.servicioventa.repository.VentaDetalleRepository;
import com.example.servicioventa.service.VentaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // Calcular el total antes de guardar
        detalleVenta.setTotal(detalleVenta.getPrecio_unitario().multiply(new BigDecimal(detalleVenta.getCantidad())));
        return ventaDetalleRepository.save(detalleVenta);
    }

    @Override
    public Detalle_Venta actualizar(Detalle_Venta detalleVenta) {
        return ventaDetalleRepository.save(detalleVenta);
    }

    @Override
    public Optional<Detalle_Venta> listarPorId(Integer id) {
        return ventaDetalleRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!ventaDetalleRepository.existsById(id)) {
            throw new RuntimeException("VentaDetalle no encontrado con ID: " + id);
        }
        ventaDetalleRepository.deleteById(id);
    }
}
