package com.agrosellnova.Agrosellnova.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTO")
    private Long id;

    @Column(name = "USUARIO_CAMPESINO")
    private String usuarioCampesino;

    @Column(name = "PRODUCTO_IMAGEN")
    private String imagen;

    @Column(name = "NOMBRE_PRODUCTO")
    private String nombre;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "PRECIO")
    private double precio;

    @Column(name = "PESO_KG")
    private double pesoKg;

    @Column(name = "STOCK")
    private int stock;

    @Column(name = "FECHA_COSECHA")
    private LocalDate fechaCosecha;

    @Column(name = "estado")
    private String estado;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioCampesino() {
        return usuarioCampesino;
    }
    public void setUsuarioCampesino(String usuarioCampesino) {
        this.usuarioCampesino = usuarioCampesino;
    }

    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPesoKg() {
        return pesoKg;
    }
    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDate getFechaCosecha() {
        return fechaCosecha;
    }
    public void setFechaCosecha(LocalDate fechaCosecha) {
        this.fechaCosecha = fechaCosecha;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
