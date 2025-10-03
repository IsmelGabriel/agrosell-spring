package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
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
    private ProductoRepository productoRepository;

    @Autowired
    private EmailService emailService;

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
        emailService.sendBookingConfirmationEmail(reserva.getUsuarioCorreo(), reserva.getUsuarioCliente(), reserva.getProducto(), reserva.getFechaReserva().toString());
    }

    public List<Producto> obtenerProductosParaReserva() {
        return productoRepository.findProductosParaReserva();
    }

    @Override
    public List<Reserva> findByUsuarioDocumento(String documentoUsuario) {
        return List.of();
    }

    @Override
    public List<Reserva> obtenerReservasPorUsuario(String usuario) {
        return reservaRepository.findByUsuarioCliente(usuario);
    }

    @Override
    public List<Reserva> filtrarReservas(String usuario, String criterio, String valor) {
        return List.of();
    }

    @Override
    public List<Reserva> buscarPorUsuario(String usuario) {
        return reservaRepository.findByUsuarioClienteContainingIgnoreCase(usuario);
    }

    @Override
    public List<Reserva> buscarPorProducto(String producto) {
        return reservaRepository.findByProductoContainingIgnoreCase(producto);
    }

    @Override
    public List<Reserva> buscarPorDocumento(String documento) {
        return reservaRepository.findByUsuarioDocumentoContainingIgnoreCase(documento);
    }
}

