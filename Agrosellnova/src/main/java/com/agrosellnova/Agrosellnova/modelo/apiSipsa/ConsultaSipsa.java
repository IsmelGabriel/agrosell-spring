package com.agrosellnova.Agrosellnova.modelo.apiSipsa;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "consultas_sipsa")
public class ConsultaSipsa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "producto")
    private String producto;

    @Column(name = "codigo_producto")
    private Integer codigoProducto;

    @Column(name = "precio_promedio")
    private Double precioPromedio;

    @Column(name = "fecha_captura")
    private LocalDateTime fechaCaptura;

    @Column(name = "fecha_consulta")
    private LocalDateTime fechaConsulta;

    @Column(name = "tipo_consulta")
    private String tipoConsulta; // "ciudad", "mes", "semana", etc.

    @Column(name = "cantidad_kg")
    private Double cantidadKg;

    @Column(name = "fuente")
    private String fuente;

    public ConsultaSipsa() {
        this.fechaConsulta = LocalDateTime.now();
    }

    public ConsultaSipsa(String ciudad, String producto, Integer codigoProducto,
                         Double precioPromedio, LocalDateTime fechaCaptura) {
        this.ciudad = ciudad;
        this.producto = producto;
        this.codigoProducto = codigoProducto;
        this.precioPromedio = precioPromedio;
        this.fechaCaptura = fechaCaptura;
        this.fechaConsulta = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Integer codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public Double getPrecioPromedio() {
        return precioPromedio;
    }

    public void setPrecioPromedio(Double precioPromedio) {
        this.precioPromedio = precioPromedio;
    }

    public LocalDateTime getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(LocalDateTime fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public LocalDateTime getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDateTime fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public Double getCantidadKg() {
        return cantidadKg;
    }

    public void setCantidadKg(Double cantidadKg) {
        this.cantidadKg = cantidadKg;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }
}
