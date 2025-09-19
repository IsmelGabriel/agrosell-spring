package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Calificaciones;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.repositorio.CalificacionesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalificacionesServiceImpl implements CalificacionesService {

    private final CalificacionesRepository calificacionesRepository;

    public CalificacionesServiceImpl(CalificacionesRepository calificacionesRepository) {
        this.calificacionesRepository = calificacionesRepository;
    }

    @Override
    public Calificaciones guardar(Calificaciones calificacion) {
        return calificacionesRepository.save(calificacion);
    }

    @Override
    public List<Calificaciones> listarPorProducto(Producto producto) {
        return calificacionesRepository.findByProducto(producto);
    }

    @Override
    public List<Calificaciones> listarPorUsuario(Long usuarioId) {
        return calificacionesRepository.findByUsuarioId(usuarioId);
    }
}
