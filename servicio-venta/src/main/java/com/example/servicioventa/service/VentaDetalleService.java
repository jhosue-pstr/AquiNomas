package com.example.servicioventa.service;

import com.example.servicioventa.entity.VentaDetalle;

import java.util.List;
import java.util.Optional;

public interface VentaDetalleService {
    public List<VentaDetalle> listar();
    public VentaDetalle guardar(VentaDetalle matricula);
    public VentaDetalle actualizar(VentaDetalle matricula);
    public Optional<VentaDetalle> listarPorId(Integer id);
    public void eliminarPorId(Integer id);

}
