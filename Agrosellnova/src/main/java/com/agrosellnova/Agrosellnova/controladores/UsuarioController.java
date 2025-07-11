package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/private")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("forms/actualizar_datos_perfil")
    public String mostrarFormularioActualizar(HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);
        model.addAttribute("usuario", usuario);
        return "forms/actualizar_datos_perfil";
    }

    @PostMapping("/actualizar_datos_perfil")
    public String actualizarPerfil(@ModelAttribute("usuario") Usuario usuarioActualizado,
                                   HttpSession session,
                                   Model model) {

        String nombreUsuario = (String) session.getAttribute("usuario");
        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuarioExistente = usuarioService.buscarPorNombreUsuario(nombreUsuario);
        usuarioActualizado.setId(usuarioExistente.getId());
        usuarioActualizado.setRol(usuarioExistente.getRol()); // conservar el rol
        usuarioActualizado.setPassword(usuarioExistente.getPassword()); // conservar la contraseña

        usuarioService.actualizarPerfil(usuarioActualizado);
        model.addAttribute("mensaje", "Perfil actualizado correctamente.");
        return "redirect:/private/perfil";
    }

}
