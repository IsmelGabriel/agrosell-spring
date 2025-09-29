package com.agrosellnova.Agrosellnova.dto;

import java.util.Map;

public class DashboardDTO {
    private Long totalVentas;
    private Long totalProductos;
    private Long totalUsuarios;

    private Map<String, Long> ventasPorFecha;
    private Map<String, Long> productosPorEstado;
    private Map<String, Long> usuariosPorRol;
    private Map<String, Long> usuariosPorEstado;

    // Getters y Setters
    public Long getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(Long totalVentas) {
        this.totalVentas = totalVentas;
    }

    public Long getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(Long totalProductos) {
        this.totalProductos = totalProductos;
    }

    public Long getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(Long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public Map<String, Long> getVentasPorFecha() {
        return ventasPorFecha;
    }

    public void setVentasPorFecha(Map<String, Long> ventasPorFecha) {
        this.ventasPorFecha = ventasPorFecha;
    }

    public Map<String, Long> getProductosPorEstado() {
        return productosPorEstado;
    }

    public void setProductosPorEstado(Map<String, Long> productosPorEstado) {
        this.productosPorEstado = productosPorEstado;
    }

    public Map<String, Long> getUsuariosPorRol() {
        return usuariosPorRol;
    }

    public void setUsuariosPorRol(Map<String, Long> usuariosPorRol) {
        this.usuariosPorRol = usuariosPorRol;
    }

    public Map<String, Long> getUsuariosPorEstado() {
        return usuariosPorEstado;
    }

    public void setUsuariosPorEstado(Map<String, Long> usuariosPorEstado) {
        this.usuariosPorEstado = usuariosPorEstado;
    }
}
