package com.agrosellnova.Agrosellnova.repositorio;


import com.agrosellnova.Agrosellnova.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByVendedor_NombreUsuario(String nombreUsuario);

    List<Venta> findByIdVentaAndVendedor_NombreUsuario(Long idVenta, String nombreUsuario);

    List<Venta> findByProducto_NombreContainingIgnoreCaseAndVendedor_NombreUsuario(String nombreProducto, String nombreUsuario);

    List<Venta> findByComprador_NombreUsuarioContainingIgnoreCaseAndVendedor_NombreUsuario(String nombreComprador, String nombreUsuario);

    List<Venta> findByFechaVentaAndVendedor_NombreUsuario(LocalDate fecha, String nombreUsuario);
}