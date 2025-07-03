package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Reserva;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioCliente(String UsuarioCliente);
    List<Reserva> findByUsuarioClienteAndIdReserva(String usuarioCliente, Long idReserva);
    List<Reserva> findByUsuarioClienteAndProductoContainingIgnoreCase(String usuarioCliente, String producto);
    List<Reserva> findByUsuarioClienteAndFechaReserva(String usuarioCliente, LocalDate fechaReserva);

}
