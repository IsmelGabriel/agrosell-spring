package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByVendedor_NombreUsuario(String nombreUsuario);

    List<Venta> findByIdVentaAndVendedor_NombreUsuario(Long id, String nombreUsuario);

    List<Venta> findByVendedor_NombreUsuarioAndProducto_NombreContainingIgnoreCase(String nombreUsuario, String producto);

    List<Venta> findByVendedor_NombreUsuarioAndFechaVenta(LocalDate fecha, String nombreUsuario);

    List<Venta> findByVendedor_NombreUsuarioAndComprador_NombreUsuarioContainingIgnoreCase(String nombreUsuario, String comprador);
}


