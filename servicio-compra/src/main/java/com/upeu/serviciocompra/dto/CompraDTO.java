package com.upeu.serviciocompra.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompraDto {

    private String codigo;
    private LocalDateTime fecha;
    private Double total;
    private Integer proveedorId;  // Este es el ID del proveedor que se va a asociar
}
