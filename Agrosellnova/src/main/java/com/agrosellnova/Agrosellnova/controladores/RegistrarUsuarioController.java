package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrarUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/public/registrarse")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "public/registrarse";
    }

    @PostMapping("/public/registrarse")
    public String procesarRegistro(@ModelAttribute("usuario") Usuario usuario, Model model) {
        String error = usuarioService.registrarUsuario(usuario);
        if (error != null) {
            model.addAttribute("error", error);
            return "public/registro_fallido";
        }
        return "redirect:/public/registro_exitoso";
    }
}
