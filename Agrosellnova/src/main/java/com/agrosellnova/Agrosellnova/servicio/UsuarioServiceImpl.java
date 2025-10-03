package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.repositorio.UsuarioRepository;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private StringHttpMessageConverter stringHttpMessageConverter;

    @Override
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
        if (usuario.getEstado() == null || usuario.getEstado().isBlank()) {
            usuario.setEstado("Habilitado");
        }

        usuarioRepository.save(usuario);

        emailService.sendWelcomeEmail(usuario.getCorreo(), usuario.getNombreUsuario());

        return null;
    }

    @Override
    public String obtenerUsuarioPorEmail(String email) {
        return "";
    }

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    @Override
    public Usuario autenticarUsuario(String usuario, String passwordPlano) {
        Usuario u = usuarioRepository.findByNombreUsuario(usuario);
        if (u != null && passwordEncoder.matches(passwordPlano, u.getPassword())) {
            return u;
        }
        return null;
    }

    @Override
    public void actualizarRolUsuario(Long Id, String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(Id).orElse(null);
        if (usuario != null) {
            usuario.setRol(nuevoRol);

            usuarioRepository.save(usuario);
        }
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuariosOrdenPorIdDesc() {
        return usuarioRepository.findAllByOrderByIdDesc();
    }

    @Override
    public void eliminarUsuarioPorId(Long idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }


    @Override
    public void actualizarEstado(Long idUsuario, String estado) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(estado);
            usuarioRepository.save(usuario);
            emailService.sendEstadoUpdateEmail(usuario.getCorreo(), usuario.getNombreUsuario(), estado);
        }
    }

    @Override
    public void actualizarRol(Long idUsuario, String nuevoRol) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setRol(nuevoRol);
            emailService.sendRoleUpdateEmail(usuario.getCorreo(), usuario.getNombreUsuario(), nuevoRol);
            usuarioRepository.save(usuario);
        }
    }

    @Override
    public void actualizarPerfil(Usuario usuarioActualizado) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioActualizado.getId());
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setNombreUsuario(usuarioActualizado.getNombreUsuario());
            usuario.setDocumento(usuarioActualizado.getDocumento());
            usuario.setDireccion(usuarioActualizado.getDireccion());
            usuario.setCorreo(usuarioActualizado.getCorreo());
            usuario.setMetodoPago(usuarioActualizado.getMetodoPago());
            usuario.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());
            usuarioRepository.save(usuario);
        }
    }


    @Override
    public Long obtenerTotalUsuarios() {
        return usuarioRepository.count();
    }

    @Override
    public Long obtenerUsuariosPorRol(String rol) {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getRol().equalsIgnoreCase(rol))
                .count();
    }

    @Override
    public Long obtenerUsuariosPorEstado(String estado) {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getEstado() != null && u.getEstado().equalsIgnoreCase(estado))
                .count();
    }

    @Override
    public List<Object[]> obtenerUsuariosAgrupadosPorRol() {
        return usuarioRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Usuario::getRol,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .toList();
    }

    @Override
    public List<Object[]> obtenerUsuariosAgrupadosPorEstado() {
        return usuarioRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Usuario::getEstado,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .toList();
    }

}
