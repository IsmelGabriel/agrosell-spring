package com.agrosellnova.Agrosellnova.controladores;

import jakarta.servlet.http.HttpSession;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaginaController {

    @GetMapping("/{pagina}")
    public String mostrarPaginaPublica(@PathVariable("pagina") String pagina, HttpSession session, Model model) {
        // Obtener nombre del usuario de la sesión
        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        // Puedes restringir algunas páginas si es necesario
        if (pagina.equals("cerrar_sesion") || pagina.equals("index") || pagina.equals("api")) {
            return "redirect:/error";
        }

        // Retorna la vista desde templates/public/pagina.html
        return "public/" + pagina;
    }

    @GetMapping("/{pagina}")
    public String mostrarPaginaForms(@PathVariable("pagina") String pagina, HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        if (pagina.equals("cerrar_sesion") || pagina.equals("index")) {
            return "redirect:/error";
        }

        return "form/" + pagina;
    }

    @GetMapping("/{pagina}")
    public String mostrarPaginaPrivate(@PathVariable("pagina") String pagina, HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        if (pagina.equals("cerrar_sesion") || pagina.equals("index")) {
            return "redirect:/error";
        }
        return "form/" + pagina;
    }
}