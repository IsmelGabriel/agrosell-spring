package com.agrosellnova.Agrosellnova.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "pqrs")
public class Pqrs {

    public enum Estado {
        PENDIENTE,
        RESUELTO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PQRS")
    private Long idPqrs;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "CORREO")
    private String correo;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "ESTADO")
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "MENSAJE", length = 1000)
    private String mensaje;

    @Column(name = "RESPUESTA", length = 1000)
    private String respuesta;

    // Getters y Setters
    public Long getIdPqrs() {
        return idPqrs;
    }

    public void setIdPqrs(Long idPqrs) {
        this.idPqrs = idPqrs;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Estado getEstado() {
        return estado;
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

}
