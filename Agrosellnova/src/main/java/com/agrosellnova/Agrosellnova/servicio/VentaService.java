package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import java.util.List;

public interface VentaService {
    List<Venta> obtenerTodasLasVentas();
    void guardarVenta(Venta venta);
    Venta obtenerVentaPorId(Long id);
    List<Venta> obtenerVentasPorProductor(String productor);
    List<Venta> findByComprador_NombreUsuario(String nombreUsuario);
    List<Venta> filtrarVentas(String productor, String criterio, String valor);
    List<Venta> filtrarCompras(String comprador, String criterio, String valor);
    List<Venta> filtrarVentasAdmin(String criterio, String valor);
    List<Venta> obtenerComprasPorUsuario(String documentoUsuario);
    List<Venta> sobtenerComprasPorUsuario(String documentoUsuario);
    List<Venta> obtenerTodas();

    Long obtenerTotalVentas(); // suma de totalVenta
    Long obtenerCantidadProductosVendidos(); // suma de cantidadKg
    Long obtenerCantidadClientes(); // clientes distintos

    List<Object[]> obtenerVentasPorMes(); // mes - total
    List<Object[]> obtenerProductosMasVendidos(); // producto - cantidad

    List<Object[]> obtenerVentasMensuales();

}

