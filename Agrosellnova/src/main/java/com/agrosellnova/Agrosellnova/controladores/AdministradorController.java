package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class AdministradorController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/private/usuarios_registrados")
    public String verUsuarios(org.springframework.ui.Model model, HttpSession session) {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        model.addAttribute("usuarios", usuarios);


        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("rol", session.getAttribute("rol"));
        return "private/usuarios_registrados";
    }

    @GetMapping("/private/eliminar_usuario")
    public String eliminarUsuario(@RequestParam("id") Long idUsuario, HttpSession session) {
        // Verifica que alguien haya iniciado sesión (opcional pero recomendable)
        if (session.getAttribute("usuario") == null) {
            return "redirect:/public/index";
        }


        usuarioService.eliminarUsuarioPorId(idUsuario);


        return "redirect:/private/usuarios_registrados";
    }

    @GetMapping("/private/actualizar_roles")
    public String mostrarActualizarRoles(Model model, HttpSession session) {
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("rol", session.getAttribute("rol"));
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "private/actualizar_roles";
    }

    @PostMapping("/private/actualizarRol")
    public String actualizarRol(@RequestParam("id_usuario") Long idUsuario,
                                @RequestParam("nuevo_rol") String nuevoRol) {

        usuarioService.actualizarRol(idUsuario, nuevoRol);
        return "redirect:/private/actualizar_roles";
    }

}
