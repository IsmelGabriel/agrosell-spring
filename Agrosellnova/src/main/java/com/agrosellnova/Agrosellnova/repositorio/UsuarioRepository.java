package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByCorreo(String correo);
    Usuario findByNombreUsuario(String nombreUsuario);
}
