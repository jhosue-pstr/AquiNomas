package com.example.servicioventa.service.impl;

import com.example.servicioventa.entity.Comprobante_pago;
import com.example.servicioventa.repository.ComprobantePagoRepository;
import com.example.servicioventa.service.ComprobantePagoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        if (comprobantePago.getTotal() == null || comprobantePago.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El total debe ser mayor a cero.");
        }

        if (!"Boleta".equalsIgnoreCase(comprobantePago.getTipo()) && !"Factura".equalsIgnoreCase(comprobantePago.getTipo())) {
            throw new IllegalArgumentException("El tipo de comprobante debe ser 'Boleta' o 'Factura'.");
        }

        // Si es Factura, el RUC del cliente es obligatorio
        if ("Factura".equalsIgnoreCase(comprobantePago.getTipo()) && (comprobantePago.getRucCliente() == null || comprobantePago.getRucCliente().isEmpty())) {
            throw new IllegalArgumentException("El RUC del cliente es obligatorio para Facturas.");
        }

        return comprobantePagoRepository.save(comprobantePago);
    }

    @Override
    public Comprobante_pago actualizar(Comprobante_pago comprobantePago) {
        if (!comprobantePagoRepository.existsById(comprobantePago.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se puede actualizar. Comprobante no encontrado con ID: " + comprobantePago.getId());
        }
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
