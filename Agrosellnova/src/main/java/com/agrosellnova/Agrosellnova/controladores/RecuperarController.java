package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RecuperarController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/recuperar_contraseña")
    public String procesarRecuperacion(
            @RequestParam("usuario") String nombreUsuario,
            @RequestParam("correo") String correo,
            @RequestParam("password") String nuevaPassword,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);

        if (usuario == null || !usuario.getCorreo().equalsIgnoreCase(correo)) {
            redirectAttributes.addFlashAttribute("error", "Usuario o correo no válidos.");
            return "redirect:/public/registro_fallido";
        }


        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("mensaje", "Contraseña actualizada con éxito. Inicia sesión.");
        return "redirect:/public/registro_exitoso";
    }
}
