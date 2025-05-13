package com.example.servicioventa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class VentaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer venta_id;
    private Integer producto_id;
    private Integer cantidad;
    private BigDecimal precio_unitario;
    private BigDecimal total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVenta_id() {
        return venta_id;
    }

    public void setVenta_id(Integer venta_id) {
        this.venta_id = venta_id;
    }

    public Integer getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(Integer producto_id) {
        this.producto_id = producto_id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public VentaDetalle() {
    }

    @Override
    public String toString() {
        return "VentaDetalle{" +
                "id=" + id +
                ", venta_id=" + venta_id +
                ", producto_id=" + producto_id +
                ", cantidad=" + cantidad +
                ", precio_unitario=" + precio_unitario +
                ", total=" + total +
                '}';
    }

    public VentaDetalle(Integer id, Integer venta_id, Integer producto_id, Integer cantidad, BigDecimal precio_unitario, BigDecimal total) {
        this.id = id;
        this.venta_id = venta_id;
        this.producto_id = producto_id;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.total = total;
    }
}
