package com.example.servicioventa.service;

import com.example.servicioventa.entity.Detalle_Venta;

import java.util.List;
import java.util.Optional;

public interface VentaDetalleService {
    public List<Detalle_Venta> listar();
    public Detalle_Venta guardar(Detalle_Venta matricula);
    public Detalle_Venta actualizar(Detalle_Venta matricula);
    public Optional<Detalle_Venta> listarPorId(Integer id);
    public void eliminarPorId(Integer id);

}
