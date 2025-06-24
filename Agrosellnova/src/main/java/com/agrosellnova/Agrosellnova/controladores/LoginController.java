package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/public/index")
    public String mostrarLogin() {
        return "public/index";
    }

    @PostMapping("/public/index")
    public String procesarLogin(@RequestParam String usuario,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {

        Usuario usuarioAutenticado = usuarioService.autenticarUsuario(usuario, password);

        if (usuarioAutenticado != null) {
            session.setAttribute("usuario", usuarioAutenticado.getNombreUsuario());
            session.setAttribute("rol", usuarioAutenticado.getRol());
            System.out.println("Inicio de sesión exitoso:");
            System.out.println("Nombre de usuario en sesión: " + session.getAttribute("usuario"));
            System.out.println("Rol en sesión: " + session.getAttribute("rol"));
            return "redirect:/public/inicio";

        }

        model.addAttribute("error", "Usuario o contraseña incorrectos.");
        return "public/session_fallida";

    }

    @GetMapping("/public/cerrar_sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/public/inicio";
    }
}
