package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.servicio.PqrsService;
import com.agrosellnova.Agrosellnova.servicio.ReservaService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import com.agrosellnova.Agrosellnova.servicio.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdministradorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PqrsService pqrsService;


    @GetMapping("/private/usuarios_registrados")
    public String verUsuarios(Model model, HttpSession session) {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        model.addAttribute("usuarios", usuarios);


        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("rol", session.getAttribute("rol"));
        return "private/usuarios_registrados";
    }

    @GetMapping("/private/eliminar_usuario")
    public String eliminarUsuario(@RequestParam("id") Long idUsuario, HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/public/index";
        }

        usuarioService.eliminarUsuarioPorId(idUsuario);

        return "redirect:/private/usuarios_registrados";
    }

    @GetMapping("/private/actualizar_roles")
    public String mostrarActualizarRoles(Model model, HttpSession session) {
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("rol", session.getAttribute("rol"));
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "private/actualizar_roles";
    }

    @PostMapping("/private/actualizarRol")
    public String actualizarRol(@RequestParam("id_usuario") Long idUsuario,
                                @RequestParam("nuevo_rol") String nuevoRol) {

        usuarioService.actualizarRol(idUsuario, nuevoRol);
        return "redirect:/private/actualizar_roles";
    }

    @GetMapping("/private/reporte_pqrs_admin")
    public String mostrarPqrs(Model model, HttpSession session) {
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("rol", session.getAttribute("rol"));
        model.addAttribute("pqrsList", pqrsService.listarTodas());
        return "private/reporte_pqrs";
    }

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/private/reporte_reservas")
    public String mostrarReporteReservas(Model model, HttpSession session) {
        List<Reserva> reservas = reservaService.obtenerTodasLasReservas();
        model.addAttribute("reservas", reservas);
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("rol", session.getAttribute("rol"));

        return "private/reporte_reservas";
    }

    @Autowired
    private VentaService ventaService;

    @GetMapping("/private/reporte_ventas")
    public String mostrarVentasFiltradas(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            HttpSession session,
            Model model) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null || !rol.equals("administrador")) {
            return "redirect:/public/index";
        }

        List<Venta> ventas;
        if (criterio != null && valor != null && !valor.isBlank()) {
            ventas = ventaService.filtrarVentasAdmin(criterio, valor);
        } else {
            ventas = ventaService.obtenerTodasLasVentas();
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("ventas", ventas);

        return "private/reporte_ventas";
    }

}
