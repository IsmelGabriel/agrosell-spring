package com.agrosellnova.Agrosellnova.repositorio.apiSipsa;

import com.agrosellnova.Agrosellnova.modelo.apiSipsa.SipsaDatoMensual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SipsaDatoMensualRepository extends JpaRepository<SipsaDatoMensual, Long> {

    List<SipsaDatoMensual> findAllByOrderByFechaConsultaDesc();

    List<SipsaDatoMensual> findByProductoContainingIgnoreCaseOrderByFechaConsultaDesc(String producto);

    @Query("SELECT d FROM SipsaDatoMensual d WHERE d.fechaMesInicio >= :fechaInicio ORDER BY d.fechaMesInicio DESC")
    List<SipsaDatoMensual> findRecientes(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT d FROM SipsaDatoMensual d WHERE " +
            "(:producto IS NULL OR LOWER(d.producto) LIKE LOWER(CONCAT('%', :producto, '%'))) AND " +
            "(:fuente IS NULL OR LOWER(d.fuente) LIKE LOWER(CONCAT('%', :fuente, '%'))) AND " +
            "(:fechaDesde IS NULL OR d.fechaMesInicio >= :fechaDesde) AND " +
            "(:fechaHasta IS NULL OR d.fechaMesInicio <= :fechaHasta) " +
            "ORDER BY d.fechaMesInicio DESC")
    List<SipsaDatoMensual> buscarConFiltros(
            @Param("producto") String producto,
            @Param("fuente") String fuente,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta
    );
}
