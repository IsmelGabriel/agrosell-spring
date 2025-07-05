package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByVendedor_NombreUsuario(String nombreUsuario);

    List<Venta> findByIdVentaAndVendedor_NombreUsuario(Long id, String nombreUsuario);

    List<Venta> findByVendedor_NombreUsuarioAndProducto_NombreContainingIgnoreCase(String nombreUsuario, String producto);

    // ✅ Corregido el orden de parámetros
    List<Venta> findByVendedor_NombreUsuarioAndFechaVenta(String nombreUsuario, LocalDate fecha);

    List<Venta> findByVendedor_NombreUsuarioAndComprador_NombreUsuarioContainingIgnoreCase(String nombreUsuario, String comprador);
}

