package com.example.servicioventa.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer cliente_id;
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detalle_Venta> detallesVenta;
    @Column(nullable = false)
    private LocalDateTime fecha_venta;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    @Column(nullable = false, length = 50)
    private String estado; // Pendiente, Pagado, Anulado

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

    public List<Detalle_Venta> getDetallesVenta() {
        return detallesVenta;
    }

    public void setDetallesVenta(List<Detalle_Venta> detallesVenta) {
        this.detallesVenta = detallesVenta;
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
                ", detallesVenta=" + detallesVenta +
                ", fecha_venta=" + fecha_venta +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                '}';
    }

    public Venta(Integer id, Integer cliente_id, List<Detalle_Venta> detallesVenta, LocalDateTime fecha_venta, BigDecimal total, String estado) {
        this.id = id;
        this.cliente_id = cliente_id;
        this.detallesVenta = detallesVenta;
        this.fecha_venta = fecha_venta;
        this.total = total;
        this.estado = estado;
    }

    // XD
}
