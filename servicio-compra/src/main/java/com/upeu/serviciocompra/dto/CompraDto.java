package com.upeu.serviciocompra.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class CompraDto {
    private Integer id;
    private Integer proveedorId;
    private BigDecimal total;
    private Timestamp fechaCompra;
    private List<DetalleCompraDto> detalles;
}