package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PaginaController {

    // ✅ Asegúrate de tener esta línea
    @Autowired
    private UsuarioService usuarioService;

    private final List<String> paginasRestringidas = List.of("cerrar_sesion", "api");

    @GetMapping("/public/{pagina}")
    public String mostrarPaginaPublica(@PathVariable("pagina") String pagina, HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        if (paginasRestringidas.contains(pagina)) {
            return "redirect:/error";
        }

        return "public/" + pagina;
    }

    @GetMapping("/forms/{pagina}")
    public String mostrarPaginaForms(@PathVariable("pagina") String pagina, HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        if (paginasRestringidas.contains(pagina)) {
            return "redirect:/error";
        }

        return "forms/" + pagina;
    }

    @GetMapping("/private/{pagina}")
    public String mostrarPaginaPrivada(@PathVariable("pagina") String pagina, HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        // ✅ Aquí ocurre el error si usuarioService es null
        Usuario user = usuarioService.buscarPorNombreUsuario(nombreUsuario);

        model.addAttribute("usuario", nombreUsuario);
        model.addAttribute("rol", rol);
        model.addAttribute("user", user);

        return "private/" + pagina;
    }
}
