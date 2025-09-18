package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.repositorio.UsuarioRepository;
import com.agrosellnova.Agrosellnova.servicio.PqrsService;
import com.agrosellnova.Agrosellnova.servicio.ReservaService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioServiceImpl;
import com.agrosellnova.Agrosellnova.servicio.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.agrosellnova.Agrosellnova.modelo.Pqrs;
import com.agrosellnova.Agrosellnova.repositorio.PqrsRepository;



import java.util.List;

@Controller
public class AdministradorController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private PqrsService pqrsService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping("/private/usuarios_registrados")
    public String verUsuarios(@RequestParam(required = false) String criterio,
                              @RequestParam(required = false) String valor,
                              Model model, HttpSession session) {

        if (session.getAttribute("usuario") == null || !session.getAttribute("rol").equals("administrador")) {
            return "redirect:/public/index";
        }

        List<Usuario> usuarios;

        if (criterio != null && valor != null && !valor.isBlank()) {
            switch (criterio.toLowerCase()) {
                case "id":
                    usuarios = usuarioRepository.findAllById(Long.parseLong(valor));
                    break;
                case "usuario":
                    usuarios = usuarioRepository.findByNombreUsuarioContainingIgnoreCase(valor);
                    break;
                case "documento":
                    usuarios = usuarioRepository.findByDocumentoContainingIgnoreCase(valor);
                    break;
                case "correo":
                    usuarios = usuarioRepository.findByCorreoContainingIgnoreCase(valor);
                    break;
                default:
                    usuarios = usuarioService.obtenerTodosLosUsuarios();
            }
        } else {
            usuarios = usuarioService.obtenerTodosLosUsuarios();
        }

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
    public String mostrarActualizarRoles(@RequestParam(required = false) String criterio,
                                         @RequestParam(required = false) String valor,
                                         Model model, HttpSession session) {

        if (session.getAttribute("usuario") == null || !session.getAttribute("rol").equals("administrador")) {
            return "redirect:/public/index";
        }
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());

        List<Usuario> usuarios;

        if (criterio != null && valor != null && !valor.isBlank()) {
            switch (criterio.toLowerCase()) {
                case "id":
                    usuarios = usuarioRepository.findAllById(Long.parseLong(valor));
                    break;
                case "nombre":
                    usuarios = usuarioRepository.findByNombreContainingIgnoreCase(valor);
                    break;
                case "documento":
                    usuarios = usuarioRepository.findByDocumentoContainingIgnoreCase(valor);
                    break;
                case "correo":
                    usuarios = usuarioRepository.findByCorreoContainingIgnoreCase(valor);
                    break;
                default:
                    usuarios = usuarioService.obtenerTodosLosUsuarios();
            }
        } else {
            usuarios = usuarioService.obtenerTodosLosUsuarios();
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("rol", session.getAttribute("rol"));



        return "private/actualizar_roles";
    }


    @PostMapping("/private/actualizarRol")
    public String actualizarRol(@RequestParam("id_usuario") Long idUsuario,
                                @RequestParam("nuevo_rol") String nuevoRol) {

        usuarioService.actualizarRol(idUsuario, nuevoRol);
        return "redirect:/private/actualizar_roles";
    }


    @Autowired
    private PqrsRepository pqrsRepository;

    @GetMapping("/private/reporte_pqrs")
    public String mostrarReportePQRS(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            Model model,
            HttpSession session
    ) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null || !rol.equals("administrador")) {
            return "redirect:/public/index";
        }

        List<Pqrs> lista;

        if (criterio != null && valor != null && !valor.isBlank()) {
            switch (criterio) {
                case "id":
                    lista = pqrsRepository.findAllByIdPqrs(Long.parseLong(valor));
                    break;
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
        return "private/reporte_pqrs";
    }

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/private/reporte_reservas")
    public String mostrarReporteReservas(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            Model model,
            HttpSession session) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null || !rol.equals("administrador")) {
            return "redirect:/public/index";
        }

        List<Reserva> reservas;

        switch (criterio != null ? criterio.toLowerCase() : "") {
            case "usuario":
                reservas = reservaService.buscarPorUsuario(valor);
                break;
            case "producto":
                reservas = reservaService.buscarPorProducto(valor);
                break;
            case "documento":
                reservas = reservaService.buscarPorDocumento(valor);
                break;
            default:
                reservas = reservaService.obtenerTodasLasReservas();
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("reservas", reservas);

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
