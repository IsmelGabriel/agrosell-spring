package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Pqrs;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PqrsRepository extends JpaRepository<Pqrs, Long> {
    List<Pqrs> findAllByIdPqrs(Long idPqrs);
    List<Pqrs> findByNombre(String nombre);
    List<Pqrs> findByCorreoContainingIgnoreCase(String correo);
    List<Pqrs> findByTelefonoContainingIgnoreCase(String telefono);
    List<Pqrs> findByEstado(Pqrs.Estado estado);
}
