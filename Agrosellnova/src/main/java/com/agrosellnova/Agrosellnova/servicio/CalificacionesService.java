package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Calificaciones;
import com.agrosellnova.Agrosellnova.modelo.Producto;

import java.util.List;

public interface CalificacionesService {
    Calificaciones guardar(Calificaciones calificacion);
    List<Calificaciones> listarPorProducto(Producto producto);
    List<Calificaciones> listarPorUsuario(Long usuarioId);
}
