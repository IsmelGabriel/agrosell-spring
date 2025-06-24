package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @GetMapping("/registrarse")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new usuario());
        return "public/registrarse";
    }

    @PostMapping("/registrarse")
    public String procesarRegistro(@ModelAttribute("usuario") usuario usuario, Model model) {
        // Aquí iría la lógica de guardado si usas una base de datos con JPA

        // Simulación:
        System.out.println("Registrado: " + usuario.getUsuario());

        // Redirige al login
        return "redirect:/index";
    }
}
