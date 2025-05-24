package com.example.servicioventa.service;

import com.example.servicioventa.entity.Detalle_Venta;
import com.example.servicioventa.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaService {
    List<Venta> listar();
    Venta guardarVenta(Venta venta, List<Detalle_Venta> detallesVenta);
    Venta actualizar(Venta venta);
    Optional<Venta> listarPorId(Integer id);
    void eliminarPorId(Integer id);
}
