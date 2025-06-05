package com.example.producto_db.dto;

import com.example.producto_db.entity.Categoria;
import com.example.producto_db.entity.Producto;

import java.math.BigDecimal;

public class ProductoDisponibleDTO {

    private Integer id;
    private String nombre;
    private Categoria categoria;
    private String descripcion;
    private BigDecimal precio;
    private Integer cantidadDisponible;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public ProductoDisponibleDTO(Producto producto, Integer cantidadDisponible) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.categoria = producto.getCategoria();
        this.descripcion = producto.getDescripcion();
        this.precio = producto.getPrecio();
        this.cantidadDisponible = cantidadDisponible;
    }


    @Override
    public String toString() {
        return "ProductoDisponibleDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", categoria=" + categoria +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", cantidadDisponible=" + cantidadDisponible +
                '}';
    }
// Getters y setters aquí
}
