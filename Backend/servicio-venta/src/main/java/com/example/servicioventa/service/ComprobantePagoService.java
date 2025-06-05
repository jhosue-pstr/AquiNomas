package com.example.servicioventa.service;


import com.example.servicioventa.entity.Comprobante_pago;

import java.util.List;
import java.util.Optional;

public interface ComprobantePagoService {
    public List<Comprobante_pago> listar();
    public Comprobante_pago guardar(Comprobante_pago matricula);
    public Comprobante_pago actualizar(Comprobante_pago matricula);
    public Optional<Comprobante_pago> listarPorId(Integer id);
    public void eliminarPorId(Integer id);
}
