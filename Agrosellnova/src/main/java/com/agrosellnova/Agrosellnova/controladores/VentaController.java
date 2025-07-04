package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.servicio.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/private")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping("/gestionar_ventas")
    public String gestionarVentas(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            HttpSession session,
            Model model) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null || !rol.equals("productor")) {
            return "redirect:/public/index";
        }

        List<Venta> listaVentas;
        if (criterio != null && valor != null && !valor.isBlank()) {
            listaVentas = ventaService.filtrarVentas(usuario, criterio, valor);
        } else {
            listaVentas = ventaService.obtenerVentasPorProductor(usuario);
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("listaVentas", listaVentas);

        return "private/gestionar_ventas";
    }
}
