package com.agrosellnova.Agrosellnova.repositorio;


import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.repositorio.CalificacionesRepository;
import com.agrosellnova.Agrosellnova.modelo.Calificaciones;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalificacionesRepository extends JpaRepository<Calificaciones, Integer> {
    List<Calificaciones> findByProducto(Producto producto);
}
