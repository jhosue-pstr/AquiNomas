package com.upeu.serviciocompra.service;

import com.upeu.serviciocompra.dto.CompraDto;
import com.upeu.serviciocompra.entity.Compra;

import java.util.List;

public interface CompraService {
    Compra crearCompra(CompraDto compraDto);
    List<Compra> obtenerTodasLasCompras();
    Compra obtenerCompraPorId(Integer id);
    Compra actualizarCompra(Integer id, CompraDto compraDto);
    void eliminarCompra(Integer id);
    Compra validarCompra(Integer id);
    CompraDto generarOrdenCompra(Integer id);
}