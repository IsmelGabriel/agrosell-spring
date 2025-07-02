package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.servicio.ReservaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/public")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Muestra el formulario para crear una reserva
    @GetMapping("/formulario_reserva")
    public String mostrarFormularioReserva(Model model) {
        model.addAttribute("reserva", new Reserva());
        return "forms/formulario_reserva";
    }

    // Guarda la reserva del usuario
    @PostMapping("/reservas/guardar")
    public String guardarReserva(@ModelAttribute("reserva") Reserva reserva, HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/public/login";
        }

        reserva.setUsuarioCliente(usuario); // ✅ Cambio aquí
        reserva.setFechaReserva(LocalDate.now());
        reservaService.guardarReserva(reserva);
        return "redirect:/public/pago_exitoso";
    }

    // Muestra el panel de control con las reservas del usuario
    @GetMapping("/panel_control")
    public String mostrarPanelControl(HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/login";
        }

        List<Reserva> listaReservas = reservaService.obtenerReservasPorUsuario(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("listaReservas", listaReservas);

        return "panel_control";
    }
}

