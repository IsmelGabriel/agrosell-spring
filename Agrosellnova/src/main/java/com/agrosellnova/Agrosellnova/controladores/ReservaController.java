package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.servicio.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.time.LocalDate;

@Controller
@RequestMapping("/public")
public class ReservaController{

    @Autowired
    private ReservaService reservaService;

    @PostMapping("/reservas/guardar")
    public String guardarReserva(@ModelAttribute("reserva") Reserva reserva) {
        reserva.setFechaReservas(LocalDate.now());
        reservaService.guardarReserva(reserva);
        return "redirect:/public/pago_exitoso";
    }
    @GetMapping("/formulario_reserva")
    public String mostrarFormularioReserva(Model model) {
        model.addAttribute("reserva", new Reserva());
        return "forms/formulario_reserva";
    }
}
