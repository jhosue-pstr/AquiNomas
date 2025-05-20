package com.example.servicioventa.service.impl;

import com.example.servicioventa.entity.Comprobante_pago;
import com.example.servicioventa.repository.ComprobantePagoRepository;
import com.example.servicioventa.service.ComprobantePagoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ComprobantePagoServiceImpl implements ComprobantePagoService {
    @Autowired
    private ComprobantePagoRepository comprobantePagoRepository;

    @Override
    public List<Comprobante_pago> listar() {

        return comprobantePagoRepository.findAll();
    }

    @Override
    public Comprobante_pago guardar(Comprobante_pago comprobantePago) {
        if (comprobantePago.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El total debe ser mayor a cero.");
        }
        return comprobantePagoRepository.save(comprobantePago);
    }


    @Override
    public Comprobante_pago actualizar(Comprobante_pago comprobantePago) {
        return comprobantePagoRepository.save(comprobantePago);
    }

    @Override
    public Optional<Comprobante_pago> listarPorId(Integer id) {
        return comprobantePagoRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!comprobantePagoRepository.existsById(id)) {
            throw new EntityNotFoundException("Comprobante no encontrado con ID: " + id);
        }
        comprobantePagoRepository.deleteById(id);
    }

}
