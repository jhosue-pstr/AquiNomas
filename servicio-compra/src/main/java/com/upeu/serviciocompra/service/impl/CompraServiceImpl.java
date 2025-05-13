package com.upeu.serviciocompra.service.impl;

import com.upeu.serviciocompra.dto.ClienteDTO;
import com.upeu.serviciocompra.dto.CompraDTO;
import com.upeu.serviciocompra.entity.Compra;
import com.upeu.serviciocompra.feign.ClienteFeign;
import com.upeu.serviciocompra.repository.CompraRepository;
import com.upeu.serviciocompra.service.CompraService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ClienteFeign clienteFeign;

    @Override
    public List<CompraDTO> listar() {
        return compraRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CompraDTO obtenerPorId(Integer id) {
        return compraRepository.findById(id).map(this::mapToDTO).orElse(null);
    }

    @Override
    public CompraDTO guardar(Compra compra) {
        return mapToDTO(compraRepository.save(compra));
    }

    @Override
    public void eliminar(Integer id) {
        compraRepository.deleteById(id);
    }

    @CircuitBreaker(name = "clienteFeign", fallbackMethod = "fallbackCliente")
    private CompraDTO mapToDTO(Compra compra) {
        ClienteDTO cliente = clienteFeign.obtenerClientePorId(compra.getClienteId());
        return CompraDTO.builder()
                .id(compra.getId())
                .clienteId(compra.getClienteId())
                .total(compra.getTotal())
                .fechaCompra(compra.getFechaCompra().toString())
                .cliente(cliente)
                .build();
    }

    public CompraDTO fallbackCliente(Compra compra, Throwable throwable) {
        return CompraDTO.builder()
                .id(compra.getId())
                .clienteId(compra.getClienteId())
                .total(compra.getTotal())
                .fechaCompra(compra.getFechaCompra().toString())
                .cliente(null) // Cliente no disponible
                .build();
    }
}