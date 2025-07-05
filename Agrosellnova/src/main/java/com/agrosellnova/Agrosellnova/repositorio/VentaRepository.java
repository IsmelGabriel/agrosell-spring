package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

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

}

