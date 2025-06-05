package com.example.servicioventa.entity;

import com.example.servicioventa.dto.Cliente;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Comprobante_pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String tipo; // "Boleta" o "Factura"

    @Column(nullable = false, length = 10)
    private String numeroSerie;

    @Column(nullable = false, length = 15)
    private String numeroComprobante;

    @Column(nullable = false)
    private LocalDateTime fechaEmision;

    private Integer cliente_id;

    @Column(length = 255)
    private String nombreCliente; // Se usa en Boleta

    @Column(length = 15)
    private String rucCliente; // Se usa en Factura

    @Column(length = 255)
    private String direccionCliente; // Se usa en Factura

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(precision = 10, scale = 2)
    private BigDecimal igv; // Se usa en Factura

    @Column(precision = 10, scale = 2)
    private BigDecimal montoNeto; // Total sin IGV (Se usa en Factura)

    @Column(nullable = false, length = 50)
    private String estado; // Pendiente, Pagado, Anulado

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Integer getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(Integer cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getRucCliente() {
        return rucCliente;
    }

    public void setRucCliente(String rucCliente) {
        this.rucCliente = rucCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getMontoNeto() {
        return montoNeto;
    }

    public void setMontoNeto(BigDecimal montoNeto) {
        this.montoNeto = montoNeto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Comprobante_pago() {
    }

    @Override
    public String toString() {
        return "Comprobante_pago{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", numeroSerie='" + numeroSerie + '\'' +
                ", numeroComprobante='" + numeroComprobante + '\'' +
                ", fechaEmision=" + fechaEmision +
                ", cliente_id=" + cliente_id +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", rucCliente='" + rucCliente + '\'' +
                ", direccionCliente='" + direccionCliente + '\'' +
                ", total=" + total +
                ", igv=" + igv +
                ", montoNeto=" + montoNeto +
                ", estado='" + estado + '\'' +
                '}';
    }

    public Comprobante_pago(Integer id, String tipo, String numeroSerie, String numeroComprobante, LocalDateTime fechaEmision, Integer cliente_id, String nombreCliente, String rucCliente, String direccionCliente, BigDecimal total, BigDecimal igv, BigDecimal montoNeto, String estado) {
        this.id = id;
        this.tipo = tipo;
        this.numeroSerie = numeroSerie;
        this.numeroComprobante = numeroComprobante;
        this.fechaEmision = fechaEmision;
        this.cliente_id = cliente_id;
        this.nombreCliente = nombreCliente;
        this.rucCliente = rucCliente;
        this.direccionCliente = direccionCliente;
        this.total = total;
        this.igv = igv;
        this.montoNeto = montoNeto;
        this.estado = estado;
    }
}
