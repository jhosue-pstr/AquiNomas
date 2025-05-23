package com.upeu.serviciocompra.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleCompraDto {
    private Integer productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal total;
}
