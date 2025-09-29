package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.dto.DashboardDTO;
import com.agrosellnova.Agrosellnova.servicio.DashboardService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("public/dashboard")
    public String dashboard(Model model, HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        DashboardDTO dashboardData = dashboardService.obtenerMetricasAdmin();
        if (dashboardData == null) {
            dashboardData = new DashboardDTO();
        }

        model.addAttribute("username", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("dashboardData", dashboardData);

        return "public/dashboard";
    }

}
