package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.repositorio.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ReservaService {
    void guardarReserva(Reserva reserva);

    List<Reserva> obtenerTodasLasReservas();

    List<Reserva> obtenerReservasPorUsuario(String usuario);
}
