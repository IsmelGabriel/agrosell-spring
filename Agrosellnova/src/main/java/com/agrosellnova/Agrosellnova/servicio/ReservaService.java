package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Reserva;

import java.util.List;

public interface ReservaService {

    void guardarReserva(Reserva reserva);
    List<Reserva> obtenerTodasLasReservas();
    List<Reserva> obtenerReservasPorUsuario(String usuario);
    List<Reserva> filtrarReservas(String usuario, String criterio, String valor);
    List<Reserva> buscarPorUsuario(String usuario);
    List<Reserva> buscarPorProducto(String producto);
    List<Reserva> buscarPorDocumento(String documento);
}


