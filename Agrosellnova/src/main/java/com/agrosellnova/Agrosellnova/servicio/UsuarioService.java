package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en el sistema.
     * @param usuario objeto con los datos del formulario.
     * @return null si el registro fue exitoso; de lo contrario, mensaje de error.
     */
    public String registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())) {
            return "El nombre de usuario ya está en uso.";
        }

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            return "El correo electrónico ya está en uso.";
        }

        String contraseñaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contraseñaEncriptada);

        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("cliente");
        }

        usuarioRepository.save(usuario);
        return null;
    }

    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public Usuario autenticarUsuario(String usuario, String passwordPlano) {
        Usuario u = usuarioRepository.findByNombreUsuario(usuario);

        if (u != null && passwordEncoder.matches(passwordPlano, u.getPassword())) {
            return u;
        }
        return null;
    }
}
