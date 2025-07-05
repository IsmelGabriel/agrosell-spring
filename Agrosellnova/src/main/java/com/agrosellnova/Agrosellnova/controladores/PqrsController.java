package com.agrosellnova.Agrosellnova.controladores;
import com.agrosellnova.Agrosellnova.modelo.Pqrs;
import com.agrosellnova.Agrosellnova.repositorio.PqrsRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


import java.util.List;

@Controller
@RequestMapping("/private")
public class PqrsController {

    @Autowired
    private PqrsRepository pqrsRepository;

    @PostMapping("/registrarPqrs")
    public String registrarPqrs(@ModelAttribute Pqrs pqrs, RedirectAttributes redirectAttrs) {
        pqrsRepository.save(pqrs);
        return "redirect:/public/ayuda";
    }

    @GetMapping("/reporte_pqrs")
    public String mostrarReportePQRS(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            Model model,
            HttpSession session
    ) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        List<Pqrs> lista;

        if (criterio != null && valor != null && !valor.isBlank()) {
            switch (criterio) {
                case "usuario":
                    lista = pqrsRepository.findByNombreContainingIgnoreCase(valor);
                    break;
                case "correo":
                    lista = pqrsRepository.findByCorreoContainingIgnoreCase(valor);
                    break;
                case "telefono":
                    lista = pqrsRepository.findByTelefonoContainingIgnoreCase(valor);
                    break;
                default:
                    lista = pqrsRepository.findAll();
            }
        } else {
            lista = pqrsRepository.findAll();
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("pqrsList", lista);
        return "reporte_pqrs";
    }
}
