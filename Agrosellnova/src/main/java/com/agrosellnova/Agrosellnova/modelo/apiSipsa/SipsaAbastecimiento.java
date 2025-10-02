package com.agrosellnova.Agrosellnova.modelo.apiSipsa;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sipsa_abastecimiento")
public class SipsaAbastecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_producto")
    private Integer codigoProducto;

    @Column(name = "producto", nullable = false)
    private String producto;

    @Column(name = "fuente", length = 500)
    private String fuente;

    @Column(name = "cantidad_toneladas")
    private Double cantidadToneladas;

    @Column(name = "fecha_mes_inicio")
    private LocalDateTime fechaMesInicio;

    @Column(name = "fecha_consulta")
    private LocalDateTime fechaConsulta;

    public SipsaAbastecimiento() {
        this.fechaConsulta = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(Integer codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public String getFuente() { return fuente; }
    public void setFuente(String fuente) { this.fuente = fuente; }

    public Double getCantidadToneladas() { return cantidadToneladas; }
    public void setCantidadToneladas(Double cantidadToneladas) { this.cantidadToneladas = cantidadToneladas; }

    public LocalDateTime getFechaMesInicio() { return fechaMesInicio; }
    public void setFechaMesInicio(LocalDateTime fechaMesInicio) { this.fechaMesInicio = fechaMesInicio; }

    public LocalDateTime getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(LocalDateTime fechaConsulta) { this.fechaConsulta = fechaConsulta; }
}

