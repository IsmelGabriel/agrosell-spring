package com.agrosellnova.Agrosellnova.modelo.apiSipsa;

import jakarta.persistence.*;

import java.time.LocalDateTime;

    @Entity
    @Table(name = "sipsa_datos_mensuales")
    public class SipsaDatoMensual {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "codigo_producto")
        private Integer codigoProducto;

        @Column(name = "producto", nullable = false)
        private String producto;

        @Column(name = "fuente", length = 500)
        private String fuente;

        @Column(name = "promedio_kg")
        private Double promedioKg;

        @Column(name = "minimo_kg")
        private Double minimoKg;

        @Column(name = "maximo_kg")
        private Double maximoKg;

        @Column(name = "fecha_mes_inicio")
        private LocalDateTime fechaMesInicio;

        @Column(name = "fecha_consulta")
        private LocalDateTime fechaConsulta;

        public SipsaDatoMensual() {
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

        public Double getPromedioKg() { return promedioKg; }
        public void setPromedioKg(Double promedioKg) { this.promedioKg = promedioKg; }

        public Double getMinimoKg() { return minimoKg; }
        public void setMinimoKg(Double minimoKg) { this.minimoKg = minimoKg; }

        public Double getMaximoKg() { return maximoKg; }
        public void setMaximoKg(Double maximoKg) { this.maximoKg = maximoKg; }

        public LocalDateTime getFechaMesInicio() { return fechaMesInicio; }
        public void setFechaMesInicio(LocalDateTime fechaMesInicio) { this.fechaMesInicio = fechaMesInicio; }

        public LocalDateTime getFechaConsulta() { return fechaConsulta; }
        public void setFechaConsulta(LocalDateTime fechaConsulta) { this.fechaConsulta = fechaConsulta; }
    }


