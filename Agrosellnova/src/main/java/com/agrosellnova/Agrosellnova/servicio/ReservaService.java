package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.modelo.Producto;

import java.util.List;

public interface ReservaService {

    // CRUD básico
    void guardarReserva(Reserva reserva);
    List<Reserva> obtenerTodasLasReservas();
    List<Reserva> obtenerReservasPorUsuario(String usuario);

    // Filtros
    List<Reserva> filtrarReservas(String usuario, String criterio, String valor);
    List<Reserva> buscarPorUsuario(String usuario);
    List<Reserva> buscarPorProducto(String producto);
    List<Reserva> buscarPorDocumento(String documento);

    // Relación con productos
    List<Producto> obtenerProductosParaReserva();

    List<Reserva> findByUsuarioDocumento(String documentoUsuario);
}
