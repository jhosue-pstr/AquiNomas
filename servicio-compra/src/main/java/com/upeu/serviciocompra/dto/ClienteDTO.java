package com.upeu.serviciocompra.dto;

import lombok.Data;

import lombok.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDTO {
    private Integer id;
    private String nombre;
    private String apellido;
    private String dni;
    private String ruc;
    private String telefono;
    private String email;
    private String direccion;
    private BigDecimal descuento;
    private Timestamp fechaRegistro;
}

