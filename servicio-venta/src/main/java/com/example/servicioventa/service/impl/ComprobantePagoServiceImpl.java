package com.example.servicioventa.service.impl;

import com.example.servicioventa.entity.Comprobante_pago;
import com.example.servicioventa.repository.ComprobantePagoRepository;
import com.example.servicioventa.service.ComprobantePagoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ComprobantePagoServiceImpl implements ComprobantePagoService {
    @Autowired
    private ComprobantePagoRepository comprobantePagoRepository;

    @Override
    public List<Comprobante_pago> listar() {
        return comprobantePagoRepository.findAll();
    }

    @Override
    public Comprobante_pago guardar(Comprobante_pago comprobantePago) {
        // Lógica adicional antes de guardar la venta (por ejemplo, calcular total)
        return comprobantePagoRepository.save(comprobantePago);
    }

    @Override
    public Comprobante_pago actualizar(Comprobante_pago venta) {
        return comprobantePagoRepository.save(venta);
    }

    @Override
    public Optional<Comprobante_pago> listarPorId(Integer id) {
        return comprobantePagoRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!comprobantePagoRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        comprobantePagoRepository.deleteById(id);
    }
}
