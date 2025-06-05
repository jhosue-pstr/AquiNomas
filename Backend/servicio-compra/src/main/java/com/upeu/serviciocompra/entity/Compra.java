package com.upeu.serviciocompra.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "proveedor_id")
    private Integer proveedorId;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha_compra", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp fechaCompra;

    @Transient
    private com.upeu.serviciocompra.dto.ProveedorDto proveedor;

    public Compra() {}

    public Compra(Integer proveedorId, BigDecimal total, Timestamp fechaCompra) {
        this.proveedorId = proveedorId;
        this.total = total;
        this.fechaCompra = fechaCompra;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Integer proveedorId) {
        this.proveedorId = proveedorId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Timestamp getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Timestamp fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public com.upeu.serviciocompra.dto.ProveedorDto getProveedor() {
        return proveedor;
    }

    public void setProveedor(com.upeu.serviciocompra.dto.ProveedorDto proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", proveedorId=" + proveedorId +
                ", total=" + total +
                ", fechaCompra=" + fechaCompra +
                ", proveedor=" + proveedor +
                '}';
    }
}
