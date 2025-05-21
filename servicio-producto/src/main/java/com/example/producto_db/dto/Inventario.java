package com.example.producto_db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Inventario {

    private Integer id;

    @JsonProperty("producto_id")
    private Integer productoId;

    @JsonProperty("cantidad_disponible")
    private Integer cantidadDisponible;

    @JsonProperty("fecha_vencimiento")
    private String fechaVencimiento;  // Fecha en formato String, ejemplo "2025-12-31"

    // Constructor vacío necesario para Jackson
    public Inventario() {}

    public Inventario(Integer id, Integer productoId, Integer cantidadDisponible, String fechaVencimiento) {
        this.id = id;
        this.productoId = productoId;
        this.cantidadDisponible = cantidadDisponible;
        this.fechaVencimiento = fechaVencimiento;
    }

    // Getters y setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", productoId=" + productoId +
                ", cantidadDisponible=" + cantidadDisponible +
                ", fechaVencimiento='" + fechaVencimiento + '\'' +
                '}';
    }
}
