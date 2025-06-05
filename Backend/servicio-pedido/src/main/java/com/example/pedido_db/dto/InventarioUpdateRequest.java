package com.example.pedido_db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventarioUpdateRequest {

    @JsonProperty("cantidad_disponible")
    private Integer cantidadDisponible;

    @JsonProperty("fecha_vencimiento")
    private String fechaVencimiento;

    public InventarioUpdateRequest() {

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

    public InventarioUpdateRequest(Integer cantidadDisponible, String fechaVencimiento) {
        this.cantidadDisponible = cantidadDisponible;
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String toString() {
        return "InventarioUpdateRequest{" +
                "cantidadDisponible=" + cantidadDisponible +
                ", fechaVencimiento='" + fechaVencimiento + '\'' +
                '}';
    }
}
