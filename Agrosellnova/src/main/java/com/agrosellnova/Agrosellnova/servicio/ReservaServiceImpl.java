package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.repositorio.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaServiceImpl(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAll();
    }

    @Override
    public void guardarReserva(Reserva reserva) {
        System.out.println("Recibido en servicio: " + reserva.getUsuarioCliente());
        reserva.setFechaReserva(LocalDate.now());
        reservaRepository.save(reserva);
    }

    @Override
    public List<Reserva> obtenerReservasPorUsuario(String usuario) {
        return reservaRepository.findByUsuarioCliente(usuario);
    }

}
