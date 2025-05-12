package com.upeu.serviciocliente.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;

@Entity
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String apellido;
    private String dni;
    private String ruc;
    private String telefono;
    private String email;
    private String direccion;

    @Column(precision = 5, scale = 2)
    private BigDecimal descuento;

    @Column(name = "fecha_registro", updatable = false)
    @CreationTimestamp
    private java.sql.Timestamp fechaRegistro;

    public Cliente() {
        // Constructor vacío
    }

    public Cliente(String nombre, String apellido, String dni, String ruc, String telefono,
                   String email, String direccion, BigDecimal descuento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.ruc = ruc;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.descuento = descuento;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", ruc='" + ruc + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", descuento=" + descuento +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
