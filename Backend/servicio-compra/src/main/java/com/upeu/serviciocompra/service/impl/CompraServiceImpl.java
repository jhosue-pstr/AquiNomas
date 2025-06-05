package com.upeu.serviciocompra.service.impl;

import com.upeu.serviciocompra.dto.CompraDto;
import com.upeu.serviciocompra.dto.DetalleCompraDto;
import com.upeu.serviciocompra.entity.Compra;
import com.upeu.serviciocompra.entity.DetalleCompra;
import com.upeu.serviciocompra.feign.ProveedorClient;
import com.upeu.serviciocompra.dto.ProveedorDto;
import com.upeu.serviciocompra.repository.CompraRepository;
import com.upeu.serviciocompra.repository.DetalleCompraRepository;
import com.upeu.serviciocompra.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ProveedorClient proveedorClient;

    @Override
    public Compra crearCompra(CompraDto compraDto) {
        Compra compra = new Compra();

        if (compraDto.getFechaCompra() == null) {
            compra.setFechaCompra(new Timestamp(System.currentTimeMillis()));
        } else {
            compra.setFechaCompra(compraDto.getFechaCompra());
        }

        compra.setTotal(compraDto.getTotal());
        compra.setProveedorId(compraDto.getProveedorId());

        Compra compraGuardada = compraRepository.save(compra);

        if (compraDto.getDetalles() != null) {
            List<DetalleCompra> detalles = compraDto.getDetalles().stream().map(det -> {
                DetalleCompra d = new DetalleCompra();
                d.setCompraId(compraGuardada.getId());
                d.setProductoId(det.getProductoId());
                d.setCantidad(det.getCantidad());
                d.setPrecioUnitario(det.getPrecioUnitario());
                d.setTotal(det.getTotal());
                return d;
            }).collect(Collectors.toList());
            detalleCompraRepository.saveAll(detalles);
        }

        try {
            System.out.println("Consultando proveedor con ID: " + compraDto.getProveedorId());
            ProveedorDto proveedor = proveedorClient.getProveedorById(compraDto.getProveedorId());
            System.out.println("Proveedor obtenido: " + proveedor);
            compraGuardada.setProveedor(proveedor);
        } catch (Exception e) {
            System.out.println("Error al consultar proveedor: " + e.getMessage());
            compraGuardada.setProveedor(null);
        }


        return compraGuardada;
    }

    @Override
    public List<Compra> obtenerTodasLasCompras() {
        List<Compra> compras = compraRepository.findAll();
        for (Compra compra : compras) {
            try {
                ProveedorDto proveedor = proveedorClient.getProveedorById(compra.getProveedorId());
                compra.setProveedor(proveedor);
            } catch (Exception e) {
                System.out.println("Error al obtener proveedor para compra ID " + compra.getId() + ": " + e.getMessage());
                compra.setProveedor(null);
            }
        }
        return compras;
    }

    @Override
    public Compra obtenerCompraPorId(Integer id) {
        Optional<Compra> compra = compraRepository.findById(id);
        compra.ifPresent(c -> {
            try {
                ProveedorDto proveedor = proveedorClient.getProveedorById(c.getProveedorId());
                c.setProveedor(proveedor);
            } catch (Exception e) {
                System.out.println("Error al obtener proveedor para compra ID " + id + ": " + e.getMessage());
                c.setProveedor(null);
            }
        });
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

            Compra compraActualizada = compraRepository.save(compra);

            try {
                compraActualizada.setProveedor(proveedorClient.getProveedorById(compraActualizada.getProveedorId()));
            } catch (Exception e) {
                compraActualizada.setProveedor(null);
            }

            return compraActualizada;
        }
        return null;
    }


    @Override
    public void eliminarCompra(Integer id) {
        detalleCompraRepository.deleteAll(detalleCompraRepository.findByCompraId(id));
        compraRepository.deleteById(id);
    }

    @Override
    public Compra validarCompra(Integer id) {
        Compra compra = compraRepository.findById(id).orElse(null);
        if (compra != null && compra.getTotal().compareTo(new java.math.BigDecimal("0")) > 0) {
            try {
                compra.setProveedor(proveedorClient.getProveedorById(compra.getProveedorId()));
            } catch (Exception e) {
                compra.setProveedor(null);
            }
            return compra;
        }
        return null;
    }


    @Override
    public CompraDto generarOrdenCompra(Integer id) {
        Compra compra = obtenerCompraPorId(id);
        if (compra == null) return null;

        List<DetalleCompra> detalles = detalleCompraRepository.findByCompraId(id);

        CompraDto dto = new CompraDto();
        dto.setId(compra.getId());
        dto.setProveedorId(compra.getProveedorId());
        dto.setFechaCompra(compra.getFechaCompra());
        dto.setTotal(compra.getTotal());
        dto.setProveedor(compra.getProveedor());

        List<DetalleCompraDto> detalleDtos = detalles.stream().map(det -> {
            DetalleCompraDto d = new DetalleCompraDto();
            d.setProductoId(det.getProductoId());
            d.setCantidad(det.getCantidad());
            d.setPrecioUnitario(det.getPrecioUnitario());
            d.setTotal(det.getTotal());
            return d;
        }).collect(Collectors.toList());

        dto.setDetalles(detalleDtos);
        return dto;
    }
}
