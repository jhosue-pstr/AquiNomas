package com.example.servicioventa.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer cliente_id;
    private LocalDateTime fecha_venta;
    private BigDecimal total;
    private String estado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(Integer cliente_id) {
        this.cliente_id = cliente_id;
    }

    public LocalDateTime getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(LocalDateTime fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Venta() {
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", cliente_id=" + cliente_id +
                ", fecha_venta=" + fecha_venta +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                '}';
    }

    public Venta(Integer id, Integer cliente_id, LocalDateTime fecha_venta, BigDecimal total, String estado) {
        this.id = id;
        this.cliente_id = cliente_id;
        this.fecha_venta = fecha_venta;
        this.total = total;
        this.estado = estado;
    }

    // XD
}
