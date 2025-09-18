package com.agrosellnova.Agrosellnova.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panel")
public class BuenasPracticasController {

    @GetMapping("/buenas-practicas")
    public String buenasPracticas() {
        return "panel/buenas_practicas"; // Ruta del template
    }
}