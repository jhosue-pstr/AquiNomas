package com.upeu.serviciocompra.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class CompraDto {

    private Integer id; // opcional si quieres incluir el ID
    private Integer proveedorId;
    private BigDecimal total;
    private Timestamp fechaCompra;

}
