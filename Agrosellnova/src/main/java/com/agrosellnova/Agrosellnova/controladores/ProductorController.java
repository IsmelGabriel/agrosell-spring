package com.agrosellnova.Agrosellnova.controladores;

import org.springframework.ui.Model;
import com.agrosellnova.Agrosellnova.modelo.Productor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductorController {

    @GetMapping("/private/ser_productor")
    public String mostrarFormulario() {
        return "/private/ser_productor";  // solo devuelve la vista
    }

}
