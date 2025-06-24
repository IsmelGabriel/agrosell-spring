package com.agrosellnova.Agrosellnova.controladores;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PaginaController {

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

    @GetMapping("/form/{pagina}")
    public String mostrarPaginaForms(@PathVariable("pagina") String pagina, HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        if (paginasRestringidas.contains(pagina)) {
            return "redirect:/error";
        }

        return "form/" + pagina;
    }

    @GetMapping("/private/{pagina}")
    public String mostrarPaginaPrivada(@PathVariable("pagina") String pagina, HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        if (usuario == null) {
            return "redirect:/public/index";
        }

        if (paginasRestringidas.contains(pagina)) {
            return "redirect:/error";
        }

        return "private/" + pagina;
    }
}
