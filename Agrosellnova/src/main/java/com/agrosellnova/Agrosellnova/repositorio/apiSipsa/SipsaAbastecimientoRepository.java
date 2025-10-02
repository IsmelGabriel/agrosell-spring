package com.agrosellnova.Agrosellnova.repositorio.apiSipsa;

import com.agrosellnova.Agrosellnova.modelo.apiSipsa.SipsaAbastecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SipsaAbastecimientoRepository extends JpaRepository<SipsaAbastecimiento, Long> {

    List<SipsaAbastecimiento> findAllByOrderByFechaConsultaDesc();

    List<SipsaAbastecimiento> findByProductoContainingIgnoreCaseOrderByFechaConsultaDesc(String producto);

    @Query("SELECT a FROM SipsaAbastecimiento a WHERE a.fechaMesInicio >= :fechaInicio ORDER BY a.fechaMesInicio DESC")
    List<SipsaAbastecimiento> findRecientes(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT a FROM SipsaAbastecimiento a WHERE " +
            "(:producto IS NULL OR LOWER(a.producto) LIKE LOWER(CONCAT('%', :producto, '%'))) AND " +
            "(:fuente IS NULL OR LOWER(a.fuente) LIKE LOWER(CONCAT('%', :fuente, '%'))) AND " +
            "(:fechaDesde IS NULL OR a.fechaMesInicio >= :fechaDesde) AND " +
            "(:fechaHasta IS NULL OR a.fechaMesInicio <= :fechaHasta) " +
            "ORDER BY a.fechaMesInicio DESC")
    List<SipsaAbastecimiento> buscarConFiltros(
            @Param("producto") String producto,
            @Param("fuente") String fuente,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta
    );
}

