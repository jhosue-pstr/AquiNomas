package com.example.servicioventa.service.impl;

import com.example.servicioventa.entity.Comprobante_pago;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.repository.ComprobantePagoRepository;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.ComprobantePagoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    @Autowired
    private VentaRepository ventaRepository;

    private static final BigDecimal IGV_RATE = BigDecimal.valueOf(0.18); // IGV del 18%
    private static final List<String> METODOS_PAGO_VALIDOS = List.of("Efectivo", "Tarjeta", "Transferencia");

    @Override
    public List<Comprobante_pago> listar() {
        return comprobantePagoRepository.findAll();
    }

    @Override
    @Transactional
    public Comprobante_pago guardar(Comprobante_pago comprobantePago) {
        // Validar que la venta asociada existe
        Venta venta = ventaRepository.findById(comprobantePago.getVenta().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada con ID: " + comprobantePago.getVenta().getId()));

        // Validar metodo de pago
        if (comprobantePago.getMetodoPago() == null || comprobantePago.getMetodoPago().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe seleccionar un método de pago válido.");
        }
        if (!METODOS_PAGO_VALIDOS.contains(comprobantePago.getMetodoPago())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Método de pago inválido. Opciones permitidas: Efectivo, Tarjeta, Transferencia.");
        }

        // Asignar total de la venta al comprobante
        comprobantePago.setTotal(venta.getTotal());

        // Calcular impuestos según el tipo de comprobante
        if ("Factura".equalsIgnoreCase(comprobantePago.getTipo())) {
            BigDecimal igv = venta.getTotal().multiply(IGV_RATE);
            comprobantePago.setIgv(igv);
            comprobantePago.setMontoNeto(venta.getTotal().subtract(igv));
        } else { // Si es Boleta, no se aplica IGV
            comprobantePago.setMontoNeto(venta.getTotal());
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