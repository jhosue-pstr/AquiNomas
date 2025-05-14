package com.upeu.serviciocompra.service.impl;

import com.upeu.serviciocompra.dto.CompraDto;
import com.upeu.serviciocompra.entity.Compra;
import com.upeu.serviciocompra.feign.ProveedorClient;
import com.upeu.serviciocompra.repository.CompraRepository;
import com.upeu.serviciocompra.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ProveedorClient proveedorClient;

    @Override
    public Compra crearCompra(CompraDto compraDto) {
        Compra compra = new Compra();
        compra.setCodigo(compraDto.getCodigo());
        compra.setFecha(compraDto.getFecha());
        compra.setTotal(compraDto.getTotal());
        compra.setProveedorId(compraDto.getProveedorId());

        // Obtener el proveedor usando Feign Client
        try {
            compra.setProveedor(proveedorClient.getProveedorById(compraDto.getProveedorId()));
        } catch (Exception e) {
            compra.setProveedor(null);  // En caso de que el proveedor no esté disponible
        }

        return compraRepository.save(compra);
    }

    @Override
    public List<Compra> obtenerTodasLasCompras() {
        return compraRepository.findAll();
    }

    @Override
    public Compra obtenerCompraPorId(Integer id) {
        Optional<Compra> compra = compraRepository.findById(id);
        return compra.orElse(null);
    }

    @Override
    public Compra actualizarCompra(Integer id, CompraDto compraDto) {
        Optional<Compra> compraOptional = compraRepository.findById(id);
        if (compraOptional.isPresent()) {
            Compra compra = compraOptional.get();
            compra.setCodigo(compraDto.getCodigo());
            compra.setFecha(compraDto.getFecha());
            compra.setTotal(compraDto.getTotal());
            compra.setProveedorId(compraDto.getProveedorId());

            // Actualizar proveedor con Feign Client
            try {
                compra.setProveedor(proveedorClient.getProveedorById(compraDto.getProveedorId()));
            } catch (Exception e) {
                compra.setProveedor(null);  // En caso de que el proveedor no esté disponible
            }

            return compraRepository.save(compra);
        }
        return null;
    }

    @Override
    public void eliminarCompra(Integer id) {
        compraRepository.deleteById(id);
    }
}
