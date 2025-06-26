package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.servicio.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping("/private/gestionar_ventas")
    public String gestionarVentas(HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || !rol.equals("productor")) {
            return "redirect:/public/index";
        }

        List<Venta> ventas = ventaService.obtenerVentasPorVendedor(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("listaVentas", ventas);

        return "private/gestionar_ventas";
    }
}
