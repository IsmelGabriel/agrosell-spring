package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Pqrs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PqrsRepository extends JpaRepository<Pqrs, Long> {
    List<Pqrs> findByNombreContainingIgnoreCase(String nombre);
    List<Pqrs> findByCorreoContainingIgnoreCase(String correo);
    List<Pqrs> findByTelefonoContainingIgnoreCase(String telefono);
}