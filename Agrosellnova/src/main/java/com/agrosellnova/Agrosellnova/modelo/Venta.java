package com.agrosellnova.Agrosellnova.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_venta")
    private Long idVenta;

    @ManyToOne
    @JoinColumn(name = "usuarios_ID_USUARIO")
    private Usuario comprador;

    @ManyToOne
    @JoinColumn(name = "usuarios_ID_VENDEDOR")
    private Usuario vendedor;

    @ManyToOne
    @JoinColumn(name = "ID_Producto")
    private Producto producto;

    @Column(name = "CANTIDAD_Kg")
    private Double cantidadKg;

    @Column(name = "TOTAL_VENTA")
    private Double totalVenta;

    @Column(name = "FECHA_VENTA")
    private LocalDate fechaVenta;

    @Column(name = "facturas_ID_factura")
    private Long idFactura;

    public Long getIdVenta() {
        return idVenta;
    }
    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public Usuario getComprador() {
        return comprador;
    }
    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    public Usuario getVendedor() {
        return vendedor;
    }
    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Double getCantidadKg() {
        return cantidadKg;
    }
    public void setCantidadKg(Double cantidadKg) {
        this.cantidadKg = cantidadKg;
    }

    public Double getTotalVenta() {
        return totalVenta;
    }
    public void setTotalVenta(Double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }
    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Long getIdFactura() {
        return idFactura;
    }
    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }
}
