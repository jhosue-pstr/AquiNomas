package com.upeu.serviciocompra.service;

import com.upeu.serviciocompra.dto.CompraDto;

import java.util.List;

public interface CompraService {
    CompraDto crearCompra(CompraDto compraDto);
    List<CompraDto> obtenerTodasLasCompras();
    CompraDto obtenerCompraPorId(Integer id);
    CompraDto actualizarCompra(Integer id, CompraDto compraDto);
    void eliminarCompra(Integer id);
    CompraDto validarCompra(Integer id);
    CompraDto generarOrdenCompra(Integer id);
}
