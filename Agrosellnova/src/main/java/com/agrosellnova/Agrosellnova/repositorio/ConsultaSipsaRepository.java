package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.apiSipsa.ConsultaSipsa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ConsultaSipsaRepository extends JpaRepository<ConsultaSipsa, Long> {

    // Obtener consultas ordenadas por fecha más reciente
    List<ConsultaSipsa> findAllByOrderByFechaConsultaDesc();

    // Buscar por ciudad
    List<ConsultaSipsa> findByCiudadOrderByFechaConsultaDesc(String ciudad);

    // Buscar por producto
    List<ConsultaSipsa> findByProductoContainingIgnoreCaseOrderByFechaConsultaDesc(String producto);

    // Buscar por tipo de consulta
    List<ConsultaSipsa> findByTipoConsultaOrderByFechaConsultaDesc(String tipoConsulta);

    // Obtener las últimas N consultas
    List<ConsultaSipsa> findTop10ByOrderByFechaConsultaDesc();

    // Buscar por rango de fechas
    @Query("SELECT c FROM ConsultaSipsa c WHERE c.fechaConsulta BETWEEN :fechaInicio AND :fechaFin ORDER BY c.fechaConsulta DESC")
    List<ConsultaSipsa> findByFechaConsultaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Obtener precios promedio por producto
    @Query("SELECT c.producto, AVG(c.precioPromedio) FROM ConsultaSipsa c GROUP BY c.producto")
    List<Object[]> obtenerPromediosPorProducto();
}
