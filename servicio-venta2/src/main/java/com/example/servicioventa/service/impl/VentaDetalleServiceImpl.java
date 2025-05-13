package com.example.servicioventa.service.impl;

import com.example.servicioventa.dto.Producto;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.entity.VentaDetalle;
import com.example.servicioventa.repository.VentaDetalleRepository;
import com.example.servicioventa.service.VentaDetalleService;
import com.example.servicioventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaDetalleServiceImpl implements VentaDetalleService {

    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;

    @Override
    public List<VentaDetalle> listar() {
        return ventaDetalleRepository.findAll();
    }

    @Override
    public VentaDetalle guardar(VentaDetalle ventaDetalle) {
        // Calcular el total antes de guardar
        ventaDetalle.setTotal(ventaDetalle.getPrecio_unitario().multiply(new BigDecimal(ventaDetalle.getCantidad())));
        return ventaDetalleRepository.save(ventaDetalle);
    }

    @Override
    public VentaDetalle actualizar(VentaDetalle ventaDetalle) {
        return ventaDetalleRepository.save(ventaDetalle);
    }

    @Override
    public Optional<VentaDetalle> listarPorId(Integer id) {
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
