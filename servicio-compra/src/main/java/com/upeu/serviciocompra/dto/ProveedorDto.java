package com.upeu.serviciocompra.dto;

import lombok.Data;

@Data
public class ProveedorDto {
    private Integer id;  // ID del proveedor como Integer
    private String nombre;
    private String telefono;
    private String direccion;
    private String email;
}

