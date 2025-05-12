package com.upeu.serviciocompra.service;

import com.upeu.serviciocompra.dto.CompraDTO;
import com.upeu.serviciocompra.entity.Compra;

import java.util.List;

public interface CompraService {
    List<CompraDTO> listar();
    CompraDTO obtenerPorId(Integer id);
    CompraDTO guardar(Compra compra);
    void eliminar(Integer id);
}
