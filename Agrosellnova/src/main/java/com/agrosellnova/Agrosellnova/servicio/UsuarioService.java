package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Usuario;

import java.util.List;

public interface UsuarioService {
    String registrarUsuario(Usuario usuario);
    Usuario buscarPorNombreUsuario(String nombreUsuario);
    Usuario autenticarUsuario(String usuario, String passwordPlano);
    void actualizarRolUsuario(Long Id, String nuevoRol);
    List<Usuario> obtenerTodosLosUsuarios();
    void eliminarUsuarioPorId(Long idUsuario);
    void actualizarRol(Long idUsuario, String nuevoRol);
    void actualizarPerfil(Usuario usuario);
}