package com.upeu.serviciocompra.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompraDTO {
    private Integer id;
    private Integer clienteId;
    private BigDecimal total;
    private String fechaCompra;
    private ClienteDTO cliente;
}
