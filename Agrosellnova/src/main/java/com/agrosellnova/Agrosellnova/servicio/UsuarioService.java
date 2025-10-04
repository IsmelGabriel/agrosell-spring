package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Usuario;

import java.util.List;

public interface UsuarioService {
    String registrarUsuario(Usuario usuario);
    String obtenerUsuarioPorEmail(String email);
    Usuario buscarPorNombreUsuario(String nombreUsuario);
    Usuario autenticarUsuario(String usuario, String passwordPlano);
    void actualizarRolUsuario(Long Id, String nuevoRol);
    List<Usuario> obtenerTodosLosUsuarios();
    List<Usuario> obtenerTodosLosUsuariosOrdenPorIdDesc();
    void eliminarUsuarioPorId(Long idUsuario);
    void actualizarRol(Long idUsuario, String nuevoRol);
    void actualizarPerfil(Usuario usuario);
    void actualizarEstado(Long idUsuario, String estado);

    // Estadísticas
    Long obtenerTotalUsuarios();
    Long obtenerUsuariosPorRol(String rol);
    Long obtenerUsuariosPorEstado(String estado);

    // Gráficos
    List<Object[]> obtenerUsuariosAgrupadosPorRol();     // rol - cantidad
    List<Object[]> obtenerUsuariosAgrupadosPorEstado();  // estado - cantidad

}