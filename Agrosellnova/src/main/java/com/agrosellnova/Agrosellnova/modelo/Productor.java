package com.agrosellnova.Agrosellnova.modelo;

import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productores")
public class Productor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_productor")
    private Long idProductor;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nombre_finca")
    private String nombreFinca;

    private String ubicacion;

    @Column(name = "area_cultivo")
    private BigDecimal areaCultivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_produccion")
    private TipoProduccion tipoProduccion;

    private String productos;

    @Column(name = "años_experiencia")
    private Integer anosExperiencia;

    @Column(name = "capacidad_produccion")
    private BigDecimal capacidadProduccion;

    @Column(name = "contacto_comercial")
    private String contactoComercial;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_solicitud")
    private EstadoSolicitud estadoSolicitud;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public enum TipoProduccion {
        Agrícola, Pecuaria, Mixta
    }

    public enum EstadoSolicitud {
        Pendiente, Aprobado, Rechazado
    }

    public Long getIdProductor() {
        return idProductor;
    }

    public void setIdProductor(Long idProductor) {
        this.idProductor = idProductor;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreFinca() {
        return nombreFinca;
    }

    public void setNombreFinca(String nombreFinca) {
        this.nombreFinca = nombreFinca;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public BigDecimal getAreaCultivo() {
        return areaCultivo;
    }

    public void setAreaCultivo(BigDecimal areaCultivo) {
        this.areaCultivo = areaCultivo;
    }

    public TipoProduccion getTipoProduccion() {
        return tipoProduccion;
    }

    public void setTipoProduccion(TipoProduccion tipoProduccion) {
        this.tipoProduccion = tipoProduccion;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public Integer getAnosExperiencia() {
        return anosExperiencia;
    }

    public void setAnosExperiencia(Integer anosExperiencia) {
        this.anosExperiencia = anosExperiencia;
    }

    public BigDecimal getCapacidadProduccion() {
        return capacidadProduccion;
    }

    public void setCapacidadProduccion(BigDecimal capacidadProduccion) {
        this.capacidadProduccion = capacidadProduccion;
    }

    public String getContactoComercial() {
        return contactoComercial;
    }

    public void setContactoComercial(String contactoComercial) {
        this.contactoComercial = contactoComercial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoSolicitud getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
