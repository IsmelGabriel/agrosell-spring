package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.servicio.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class VentaController {

    @Autowired
    private VentaService ventaService;
    @GetMapping("/gestionar_ventas")
    public String mostrarVentas(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            HttpSession session,
            Model model) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        List<Venta> listaVentas = ventaService.filtrarVentas(criterio, valor, usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("listaVentas", listaVentas);

        return "vistas_privadas/gestionar_ventas";
}

    @GetMapping("/private/gestionar_ventas")
    public String gestionarVentas(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            HttpSession session,
            Model model) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null || !rol.equalsIgnoreCase("productor")) {
            return "redirect:/public/index";
        }

        List<Venta> listaVentas;

        if (criterio != null && valor != null && !criterio.isBlank() && !valor.isBlank()) {
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
