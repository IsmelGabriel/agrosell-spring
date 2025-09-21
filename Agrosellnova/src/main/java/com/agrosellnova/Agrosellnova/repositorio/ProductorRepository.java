package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Productor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductorRepository extends JpaRepository<Productor, Long> {

    // Buscar por usuario
    Optional<Productor> findByIdUsuario(Integer idUsuario);
    List<Productor> findAllByIdUsuario(Integer idUsuario);
    boolean existsByIdUsuario(Integer idUsuario);

    // Buscar por estado de solicitud
    List<Productor> findByEstadoSolicitud(Productor.EstadoSolicitud estadoSolicitud);

    // Buscar por tipo de producción
    List<Productor> findByTipoProduccion(Productor.TipoProduccion tipoProduccion);

    // Búsquedas por nombre de finca
    List<Productor> findByNombreFincaContainingIgnoreCase(String nombreFinca);

    // Búsquedas por ubicación
    List<Productor> findByUbicacionContainingIgnoreCase(String ubicacion);

    // Búsquedas por productos
    List<Productor> findByProductosContainingIgnoreCase(String productos);

    // Buscar productores aprobados
    List<Productor> findByEstadoSolicitudOrderByFechaRegistroDesc(Productor.EstadoSolicitud estadoSolicitud);

    // Contar por estado
    long countByEstadoSolicitud(Productor.EstadoSolicitud estadoSolicitud);
}
