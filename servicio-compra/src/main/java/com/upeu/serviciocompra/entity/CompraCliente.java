package com.upeu.serviciocompra.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
public class CompraCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId; // Solo el ID del cliente, no la entidad

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha_compra", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp fechaCompra;

    public CompraCliente() {}

    public CompraCliente(Integer clienteId, BigDecimal total) {
        this.clienteId = clienteId;
        this.total = total;
    }
}
