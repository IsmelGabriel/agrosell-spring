package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.repositorio.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;
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

    @Override
    public List<Reserva> filtrarReservas(String usuario, String criterio, String valor) {
        switch (criterio.toLowerCase()) {
            case "id":
                try {
                    Long id = Long.parseLong(valor);
                    Reserva reserva = reservaRepository.findByIdReservaAndUsuarioCliente(id, usuario);
                    return reserva != null ? List.of(reserva) : List.of();
                } catch (NumberFormatException e) {
                    return List.of();
                }

            case "producto":
                return reservaRepository.findByUsuarioClienteAndProductoContainingIgnoreCase(usuario, valor);

            case "fecha":
                try {
                    LocalDate fecha = LocalDate.parse(valor);
                    return reservaRepository.findByUsuarioClienteAndFechaReserva(usuario, fecha);
                } catch (DateTimeParseException e) {
                    return List.of();
                }

            default:
                return List.of();
        }
    }

}

