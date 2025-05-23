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
    public CompraDto crearCompra(CompraDto compraDto) {
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

        // Obtener detalles guardados
        List<DetalleCompra> detallesGuardados = detalleCompraRepository.findByCompraId(compraGuardada.getId());

        CompraDto dto = new CompraDto();
        dto.setId(compraGuardada.getId());
        dto.setProveedorId(compraGuardada.getProveedorId());
        dto.setFechaCompra(compraGuardada.getFechaCompra());
        dto.setTotal(compraGuardada.getTotal());

        List<DetalleCompraDto> detalleDtos = detallesGuardados.stream().map(det -> {
            DetalleCompraDto d = new DetalleCompraDto();
            d.setProductoId(det.getProductoId());
            d.setCantidad(det.getCantidad());
            d.setPrecioUnitario(det.getPrecioUnitario());
            d.setTotal(det.getTotal());
            return d;
        }).collect(Collectors.toList());

        dto.setDetalles(detalleDtos);

        try {
            ProveedorDto proveedor = proveedorClient.getProveedorById(compraGuardada.getProveedorId());
            dto.setProveedor(proveedor);
        } catch (Exception e) {
            System.out.println("Error al obtener proveedor: " + e.getMessage());
            dto.setProveedor(null);
        }

        return dto;
    }

    @Override
    public List<CompraDto> obtenerTodasLasCompras() {
        List<Compra> compras = compraRepository.findAll();

        return compras.stream().map(compra -> {
            CompraDto dto = new CompraDto();
            dto.setId(compra.getId());
            dto.setProveedorId(compra.getProveedorId());
            dto.setFechaCompra(compra.getFechaCompra());
            dto.setTotal(compra.getTotal());

            // Obtener detalles
            List<DetalleCompra> detalles = detalleCompraRepository.findByCompraId(compra.getId());
            List<DetalleCompraDto> detalleDtos = detalles.stream().map(det -> {
                DetalleCompraDto d = new DetalleCompraDto();
                d.setProductoId(det.getProductoId());
                d.setCantidad(det.getCantidad());
                d.setPrecioUnitario(det.getPrecioUnitario());
                d.setTotal(det.getTotal());
                return d;
            }).collect(Collectors.toList());
            dto.setDetalles(detalleDtos);

            // Obtener proveedor
            try {
                ProveedorDto proveedor = proveedorClient.getProveedorById(compra.getProveedorId());
                dto.setProveedor(proveedor);
            } catch (Exception e) {
                System.out.println("Error al obtener proveedor para compra ID " + compra.getId() + ": " + e.getMessage());
                dto.setProveedor(null);
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public CompraDto obtenerCompraPorId(Integer id) {
        Optional<Compra> compra = compraRepository.findById(id);
        if (compra.isEmpty()) return null;

        Compra c = compra.get();
        CompraDto dto = new CompraDto();
        dto.setId(c.getId());
        dto.setProveedorId(c.getProveedorId());
        dto.setFechaCompra(c.getFechaCompra());
        dto.setTotal(c.getTotal());

        List<DetalleCompra> detalles = detalleCompraRepository.findByCompraId(c.getId());
        List<DetalleCompraDto> detalleDtos = detalles.stream().map(det -> {
            DetalleCompraDto d = new DetalleCompraDto();
            d.setProductoId(det.getProductoId());
            d.setCantidad(det.getCantidad());
            d.setPrecioUnitario(det.getPrecioUnitario());
            d.setTotal(det.getTotal());
            return d;
        }).collect(Collectors.toList());
        dto.setDetalles(detalleDtos);

        try {
            ProveedorDto proveedor = proveedorClient.getProveedorById(c.getProveedorId());
            dto.setProveedor(proveedor);
        } catch (Exception e) {
            System.out.println("Error al obtener proveedor para compra ID " + id + ": " + e.getMessage());
            dto.setProveedor(null);
        }

        return dto;
    }

    @Override
    public CompraDto actualizarCompra(Integer id, CompraDto compraDto) {
        Optional<Compra> compraOptional = compraRepository.findById(id);
        if (compraOptional.isEmpty()) return null;

        Compra compra = compraOptional.get();
        compra.setFechaCompra(compraDto.getFechaCompra());
        compra.setTotal(compraDto.getTotal());
        compra.setProveedorId(compraDto.getProveedorId());

        Compra compraActualizada = compraRepository.save(compra);

        // Actualizar detalles: para simplicidad, eliminamos los antiguos y guardamos los nuevos
        detalleCompraRepository.deleteAll(detalleCompraRepository.findByCompraId(id));

        if (compraDto.getDetalles() != null) {
            List<DetalleCompra> detalles = compraDto.getDetalles().stream().map(det -> {
                DetalleCompra d = new DetalleCompra();
                d.setCompraId(id);
                d.setProductoId(det.getProductoId());
                d.setCantidad(det.getCantidad());
                d.setPrecioUnitario(det.getPrecioUnitario());
                d.setTotal(det.getTotal());
                return d;
            }).collect(Collectors.toList());
            detalleCompraRepository.saveAll(detalles);
        }

        return obtenerCompraPorId(id);
    }

    @Override
    public void eliminarCompra(Integer id) {
        detalleCompraRepository.deleteAll(detalleCompraRepository.findByCompraId(id));
        compraRepository.deleteById(id);
    }

    @Override
    public CompraDto validarCompra(Integer id) {
        Compra compra = compraRepository.findById(id).orElse(null);
        if (compra != null && compra.getTotal().compareTo(new java.math.BigDecimal("0")) > 0) {
            return obtenerCompraPorId(id);
        }
        return null;
    }

    @Override
    public CompraDto generarOrdenCompra(Integer id) {
        // Este método ya hace lo mismo que obtenerCompraPorId, puedes reutilizar
        return obtenerCompraPorId(id);
    }
}
