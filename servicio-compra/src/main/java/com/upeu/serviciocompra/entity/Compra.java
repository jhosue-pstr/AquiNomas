package com.upeu.serviciocompra.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.upeu.serviciocompra.dto.ProveedorDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // ID de la compra como Integer

    private String codigo;

    private LocalDateTime fecha;

    private Double total;

    private Integer proveedorId;  // ID de proveedor como Integer

    @Transient
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProveedorDto proveedor;  // Relaciona el DTO de proveedor
}

