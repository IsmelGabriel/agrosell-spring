package com.agrosellnova.Agrosellnova.modelo;

import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productores")
public class Productor {

    public enum TipoProduccion {
        AGRÍCOLA,
        PECUARIA,
        MIXTA
    }

    public enum EstadoSolicitud {
        PENDIENTE,
        APROBADO,
        RECHAZADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_productor")
    private Long idProductor;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre_finca", nullable = false, length = 100)
    private String nombreFinca;

    @Column(name = "ubicacion", nullable = false, length = 255)
    private String ubicacion;

    @Column(name = "area_cultivo", precision = 10, scale = 2)
    private BigDecimal areaCultivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_produccion", nullable = false)
    private TipoProduccion tipoProduccion;

    @Column(name = "productos", columnDefinition = "TEXT")
    private String productos;

    @Column(name = "años_experiencia")
    private Integer aniosExperiencia;

    @Column(name = "capacidad_produccion", precision = 10, scale = 2)
    private BigDecimal capacidadProduccion;

    @Column(name = "contacto_comercial", nullable = false, length = 100)
    private String contactoComercial;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_solicitud")
    private EstadoSolicitud estadoSolicitud = EstadoSolicitud.PENDIENTE;

    @Column(name = "fecha_registro", updatable = false, insertable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion", insertable = false)
    private LocalDateTime fechaActualizacion;
}
