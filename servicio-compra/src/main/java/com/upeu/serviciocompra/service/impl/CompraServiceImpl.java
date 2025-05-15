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
        compra.setFechaCompra(compraDto.getFechaCompra());
        compra.setTotal(compraDto.getTotal());
        compra.setProveedorId(compraDto.getProveedorId());

        try {
            compra.setProveedor(proveedorClient.getProveedorById(compraDto.getProveedorId()));
        } catch (Exception e) {
            compra.setProveedor(null);
        }

        return compraRepository.save(compra);
    }


    @Override
    public List<Compra> obtenerTodasLasCompras() {
        List<Compra> compras = compraRepository.findAll();
        for (Compra compra : compras) {
            try {
                compra.setProveedor(proveedorClient.getProveedorById(compra.getProveedorId()));
            } catch (Exception e) {
                compra.setProveedor(null); // o puedes usar ProveedorFallback manual
            }
        }
        return compras;
    }


    @Override
    public Compra obtenerCompraPorId(Integer id) {
        Optional<Compra> compra = compraRepository.findById(id);
        if (compra.isPresent()) {
            try {
                compra.get().setProveedor(proveedorClient.getProveedorById(compra.get().getProveedorId()));
            } catch (Exception e) {
                compra.get().setProveedor(null);
            }
        }
        return compra.orElse(null);
    }


    @Override
    public Compra actualizarCompra(Integer id, CompraDto compraDto) {
        Optional<Compra> compraOptional = compraRepository.findById(id);
        if (compraOptional.isPresent()) {
            Compra compra = compraOptional.get();
            compra.setFechaCompra(compraDto.getFechaCompra());
            compra.setTotal(compraDto.getTotal());
            compra.setProveedorId(compraDto.getProveedorId());

            try {
                compra.setProveedor(proveedorClient.getProveedorById(compraDto.getProveedorId()));
            } catch (Exception e) {
                compra.setProveedor(null);
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
