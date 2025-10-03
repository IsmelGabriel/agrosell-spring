package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByNombreUsuarioContainingIgnoreCase(String nombreUsuario);
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    List<Usuario> findByDocumentoContainingIgnoreCase(String documento);
    List<Usuario> findByCorreoContainingIgnoreCase(String correo);
    List<Usuario> findByEstadoContainingIgnoreCase(String estado);
    List<Usuario> findAllById(Long id);
    Usuario findByNombreUsuario(String nombreUsuario);
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByCorreo(String correo);
    List<Usuario> findAll();
    List<Usuario> findCorreoByRol(String rol);
    long countByRol(String rol);
    long countByEstado(String estado);
    List<Usuario> findAllByOrderByIdDesc();
}
