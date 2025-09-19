package com.agrosellnova.Agrosellnova.servicio;


import com.agrosellnova.Agrosellnova.modelo.Calificaciones;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.repositorio.CalificacionesRepository;
import com.agrosellnova.Agrosellnova.servicio.CalificacionesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalificacionesServiceImpl implements CalificacionesService {

    private final CalificacionesRepository calificacionesRepository;

    public CalificacionesServiceImpl(CalificacionesRepository calificacionesRepository) {
        this.calificacionesRepository = calificacionesRepository;
    }

    @Override
    public Calificaciones guardar(Calificaciones calificaciones) {
        return calificacionesRepository.save(calificaciones);
    }

    @Override
    public List<Calificaciones> listarPorProducto(Producto producto) {
        return calificacionesRepository.findByProducto(producto);
    }
}