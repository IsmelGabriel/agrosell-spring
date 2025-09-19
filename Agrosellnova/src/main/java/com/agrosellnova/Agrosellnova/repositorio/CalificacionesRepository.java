package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Calificaciones;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionesRepository extends JpaRepository<Calificaciones, Long> {
    // Buscar calificaciones por producto
    List<Calificaciones> findByProducto(Producto producto);

    // Buscar calificaciones por usuario
    List<Calificaciones> findByUsuarioId(Long usuarioId);
}
