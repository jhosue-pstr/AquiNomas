package com.example.pedido_db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Inventario {

    private Integer id;

    @JsonProperty("producto_id")
    private Integer productoId;

    @JsonProperty("cantidad_disponible")
    private Integer cantidadDisponible;

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

    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", productoId=" + productoId +
                ", cantidadDisponible=" + cantidadDisponible +
                '}';
    }

    public Inventario(Integer id, Integer productoId, Integer cantidadDisponible) {
        this.id = id;
        this.productoId = productoId;
        this.cantidadDisponible = cantidadDisponible;
    }
}
