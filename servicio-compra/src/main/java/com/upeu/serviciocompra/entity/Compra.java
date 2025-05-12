package com.upeu.serviciocompra.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "compra_cliente")
@Data
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha_compra", updatable = false)
    @CreationTimestamp
    private Timestamp fechaCompra;

    public Compra() {}

    public Compra(Integer clienteId, BigDecimal total) {
        this.clienteId = clienteId;
        this.total = total;
    }
}
