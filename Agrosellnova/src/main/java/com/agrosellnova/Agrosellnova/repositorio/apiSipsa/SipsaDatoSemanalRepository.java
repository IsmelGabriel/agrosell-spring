package com.agrosellnova.Agrosellnova.repositorio.apiSipsa;

import com.agrosellnova.Agrosellnova.modelo.apiSipsa.SipsaDatoSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SipsaDatoSemanalRepository extends JpaRepository<SipsaDatoSemanal, Long> {

    List<SipsaDatoSemanal> findAllByOrderByFechaConsultaDesc();

    List<SipsaDatoSemanal> findByProductoContainingIgnoreCaseOrderByFechaConsultaDesc(String producto);

    @Query("SELECT d FROM SipsaDatoSemanal d WHERE d.fechaInicioSemana >= :fechaInicio ORDER BY d.fechaInicioSemana DESC")
    List<SipsaDatoSemanal> findRecientes(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT d FROM SipsaDatoSemanal d WHERE " +
            "(:producto IS NULL OR LOWER(d.producto) LIKE LOWER(CONCAT('%', :producto, '%'))) AND " +
            "(:fuente IS NULL OR LOWER(d.fuente) LIKE LOWER(CONCAT('%', :fuente, '%'))) AND " +
            "(:fechaDesde IS NULL OR d.fechaInicioSemana >= :fechaDesde) AND " +
            "(:fechaHasta IS NULL OR d.fechaInicioSemana <= :fechaHasta) " +
            "ORDER BY d.fechaInicioSemana DESC")
    List<SipsaDatoSemanal> buscarConFiltros(
            @Param("producto") String producto,
            @Param("fuente") String fuente,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta
    );
}
