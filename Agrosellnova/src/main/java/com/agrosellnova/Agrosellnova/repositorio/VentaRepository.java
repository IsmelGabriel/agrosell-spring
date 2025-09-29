package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByVendedor_NombreUsuario(String nombreUsuario);
    List<Venta> findByComprador_NombreUsuario(String nombreUsuario);
    List<Venta> findByIdVentaAndVendedor_NombreUsuario(Long id, String nombreUsuario);
    List<Venta> findByVendedor_NombreUsuarioAndProducto_NombreContainingIgnoreCase(String nombreUsuario, String producto);
    List<Venta> findByVendedor_NombreUsuarioAndFechaVenta(String nombreUsuario, LocalDate fecha);
    List<Venta> findByVendedor_NombreUsuarioAndComprador_NombreUsuarioContainingIgnoreCase(String nombreUsuario, String comprador);
    List<Venta> findByIdVentaAndComprador_NombreUsuario(Long id, String nombreUsuario);
    List<Venta> findByComprador_NombreUsuarioAndProducto_NombreContainingIgnoreCase(String nombreUsuario, String producto);
    List<Venta> findByComprador_NombreUsuarioAndFechaVenta(String nombreUsuario, LocalDate fecha);
    List<Venta> findByComprador_NombreUsuarioAndVendedor_NombreUsuarioContainingIgnoreCase(String nombreUsuario, String vendedor);
    List<Venta> findByComprador_NombreUsuarioContainingIgnoreCase(String cliente);
    List<Venta> findByVendedor_NombreUsuarioContainingIgnoreCase(String vendedor);
    List<Venta> findByProducto_NombreContainingIgnoreCase(String producto);
    List<Venta> findByFechaVenta(LocalDate fecha);

    // ==== Métodos agregados para Dashboard ====

    // Total en dinero de todas las ventas
    @Query("SELECT SUM(v.totalVenta) FROM Venta v")
    Double getTotalVentas();

    // Total de productos vendidos (kg)
    @Query("SELECT SUM(v.cantidadKg) FROM Venta v")
    Double getTotalProductosVendidos();

    // Cantidad de clientes únicos
    @Query("SELECT COUNT(DISTINCT v.comprador.id) FROM Venta v")
    Long getTotalClientes();

    // Ventas mensuales (para gráfico)
    @Query("SELECT MONTH(v.fechaVenta), SUM(v.totalVenta) " +
            "FROM Venta v " +
            "WHERE v.fechaVenta >= :desde " +
            "GROUP BY MONTH(v.fechaVenta) " +
            "ORDER BY MONTH(v.fechaVenta)")
    List<Object[]> getVentasMensuales(LocalDate desde);

    // Productos más vendidos (TOP N)
    @Query("SELECT v.producto.nombre, SUM(v.cantidadKg) " +
            "FROM Venta v " +
            "GROUP BY v.producto.nombre " +
            "ORDER BY SUM(v.cantidadKg) DESC")
    List<Object[]> getProductosMasVendidos();
}

