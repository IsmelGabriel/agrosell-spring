package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.servicio.ReservaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.agrosellnova.Agrosellnova.repositorio.ReservaRepository;


import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/private")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/forms/formulario_reserva")
    public String mostrarFormularioReserva(Model model) {
        model.addAttribute("reserva", new Reserva());
        return "forms/formulario_reserva";
    }

    @PostMapping("/registrarReserva")
    public String guardarReserva(@ModelAttribute("reserva") Reserva reserva, HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/public/index";
        }

        reserva.setUsuarioCliente(usuario);
        reserva.setFechaReserva(LocalDate.now());
        reservaService.guardarReserva(reserva);
        return "redirect:/public/reserva_exitosa";
    }

    @GetMapping("/gestionar_reservas")
    public String mostrarReservas(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            HttpSession session,
            Model model) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        List<Reserva> reservas;

        if (criterio != null && valor != null && !valor.isBlank()) {
            reservas = reservaService.filtrarReservas(usuario, criterio, valor);
        } else {
            reservas = reservaService.obtenerReservasPorUsuario(usuario);
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("listaReservas", reservas);

        return "private/gestionar_reservas";
    }
    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/reservas/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));
        model.addAttribute("reserva", reserva);
        return "forms/editar_reserva";
    }
    @PostMapping("/reservas/actualizar")
    public String actualizarReserva(@ModelAttribute("reserva") Reserva reserva) {
        reservaRepository.save(reserva);
        return "redirect:/vistas_privadas/gestionar_reservas.html"; // cambia si tu vista final es otra
    }

}

