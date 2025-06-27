package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Resena;
import com.agrosellnova.Agrosellnova.servicio.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/public")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @PostMapping("/registrarResena")
    public String guardarResena(@ModelAttribute Resena resena, RedirectAttributes redirectAttrs) {
        resenaService.guardar(resena);
        redirectAttrs.addFlashAttribute("mensaje", "¡Gracias por tu reseña!");
        return "redirect:/public/resena_exitosa";
    }
}
