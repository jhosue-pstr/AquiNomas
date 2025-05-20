package com.example.servicioventa.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Detalle_Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;
    private Integer pedido_id;
    private Integer cantidad;
    private BigDecimal precio_unitario;
    private BigDecimal total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Integer getPedido_id() {
        return pedido_id;
    }

    public void setPedido_id(Integer pedido_id) {
        this.pedido_id = pedido_id;
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

    public Detalle_Venta() {
    }

    @Override
    public String toString() {
        return "Detalle_Venta{" +
                "id=" + id +
                ", venta=" + venta +
                ", pedido_id=" + pedido_id +
                ", cantidad=" + cantidad +
                ", precio_unitario=" + precio_unitario +
                ", total=" + total +
                '}';
    }

    public Detalle_Venta(Integer id, Venta venta, Integer pedido_id, Integer cantidad, BigDecimal precio_unitario, BigDecimal total) {
        this.id = id;
        this.venta = venta;
        this.pedido_id = pedido_id;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.total = total;
    }
}
