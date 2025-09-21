package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Productor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductorRepository extends JpaRepository<Productor, Long> {


    Optional<Productor> findByIdUsuario(Integer idUsuario);
    List<Productor> findAllByIdUsuario(Integer idUsuario);
    boolean existsByIdUsuario(Integer idUsuario);


    List<Productor> findByEstadoSolicitud(Productor.EstadoSolicitud estadoSolicitud);


    List<Productor> findByTipoProduccion(Productor.TipoProduccion tipoProduccion);

    List<Productor> findByNombreFincaContainingIgnoreCase(String nombreFinca);


    List<Productor> findByUbicacionContainingIgnoreCase(String ubicacion);


    List<Productor> findByProductosContainingIgnoreCase(String productos);


    List<Productor> findByEstadoSolicitudOrderByFechaRegistroDesc(Productor.EstadoSolicitud estadoSolicitud);


    long countByEstadoSolicitud(Productor.EstadoSolicitud estadoSolicitud);
}
