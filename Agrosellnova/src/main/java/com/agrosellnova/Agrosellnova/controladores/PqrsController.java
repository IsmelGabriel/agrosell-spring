package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Pqrs;
import com.agrosellnova.Agrosellnova.servicio.PqrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/public")
public class PqrsController {

    @Autowired
    private PqrsService pqrsService;

    @PostMapping("/registrarPQRS")
    public String registrarPqrs(@ModelAttribute Pqrs pqrs, RedirectAttributes redirectAttrs) {
        pqrsService.guardar(pqrs);
        redirectAttrs.addFlashAttribute("mensaje", "PQRS registrada correctamente");
        return "redirect:/public/ayuda";
    }
}
