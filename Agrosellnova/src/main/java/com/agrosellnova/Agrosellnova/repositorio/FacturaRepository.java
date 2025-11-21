package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Factura;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    // Buscar factura por número de factura
    Optional<Factura> findByNumeroFactura(String numeroFactura);

    // Buscar todas las facturas de un usuario
    List<Factura> findByUsuario(Usuario usuario);

    // Buscar facturas por usuario ordenadas por fecha
    List<Factura> findByUsuarioOrderByFechaEmisionDesc(Usuario usuario);

    // Buscar facturas por estado
    List<Factura> findByEstadoFactura(String estadoFactura);

    // Buscar facturas por usuario y estado
    List<Factura> findByUsuarioAndEstadoFactura(Usuario usuario, String estadoFactura);

    // Buscar facturas por rango de fechas
    List<Factura> findByFechaEmisionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar facturas por usuario y rango de fechas
    List<Factura> findByUsuarioAndFechaEmisionBetween(Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin);

    // Obtener el último número de factura para generar el siguiente
    @Query("SELECT f FROM Factura f ORDER BY f.idFactura DESC")
    List<Factura> findTopByOrderByIdFacturaDesc();

    // Contar facturas por estado
    Long countByEstadoFactura(String estadoFactura);

    // Buscar facturas por ID de pago
    @Query("SELECT f FROM Factura f WHERE f.pago.idPago = :idPago")
    Optional<Factura> findByPagoId(Long idPago);
}