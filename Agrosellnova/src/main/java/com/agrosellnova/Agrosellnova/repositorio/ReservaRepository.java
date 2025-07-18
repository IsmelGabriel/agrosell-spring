package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioCliente(String usuario);
    Reserva findByIdReservaAndUsuarioCliente(Long id, String usuario);
    List<Reserva> findByUsuarioClienteAndProductoContainingIgnoreCase(String usuario, String producto);

    List<Reserva> findByUsuarioClienteContainingIgnoreCase(String usuario);
    List<Reserva> findByProductoContainingIgnoreCase(String producto);
    List<Reserva> findByUsuarioDocumentoContainingIgnoreCase(String documento);

}
