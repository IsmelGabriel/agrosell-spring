package com.agrosellnova.Agrosellnova.controladores;


import com.agrosellnova.Agrosellnova.modelo.Factura;
import com.agrosellnova.Agrosellnova.modelo.DetalleFactura;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.servicio.FacturaService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/private")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/mis-facturas")
    public String listarMisFacturas(HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuarioObj = usuarioService.buscarPorNombreUsuario(nombreUsuario);

        if (usuarioObj == null) {
            return "redirect:/public/index";
        }

        List<Factura> facturas = facturaService.listarPorUsuario(usuarioObj);

        model.addAttribute("facturas", facturas);
        model.addAttribute("usuario", usuarioObj); // Cambiado de "usuarioLogueado" a "usuario"

        return "private/mis_facturas";
    }

    // Ver detalle de una factura específica
    @GetMapping("/detalle/{id}")
    public String verDetalleFactura(@PathVariable Long id, HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);
        Optional<Factura> facturaOpt = facturaService.buscarPorId(id);

        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();

            // Verificar que la factura pertenece al usuario
            if (!factura.getUsuario().getId().equals(usuario.getId())) {
                return "redirect:/private/mis-facturas";
            }

            List<DetalleFactura> detalles = facturaService.obtenerDetalles(id);

            model.addAttribute("factura", factura);
            model.addAttribute("detalles", detalles);
            model.addAttribute("usuario", usuario);

            return "private/detalle_facturas";
        }

        return "redirect:/private/mis-facturas";
    }

    // Buscar factura por número
    @GetMapping("/buscar")
    public String buscarFactura(@RequestParam("numeroFactura") String numeroFactura,
                                HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);
        Optional<Factura> facturaOpt = facturaService.buscarPorNumero(numeroFactura);

        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();

            // Verificar que la factura pertenece al usuario
            if (!factura.getUsuario().getId().equals(usuario.getId())) {
                model.addAttribute("error", "No tienes permiso para ver esta factura");
                return "redirect:/private/mis-facturas";
            }

            return "redirect:/private/detalle/" + factura.getIdFactura();
        }

        model.addAttribute("error", "Factura no encontrada");
        return "redirect:/private/mis-facturas";
    }

    // Filtrar facturas por estado
    @GetMapping("/filtrar")
    public String filtrarPorEstado(@RequestParam("estado") String estado,
                                   HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);
        List<Factura> facturas = facturaService.listarPorUsuarioYEstado(usuario, estado);

        model.addAttribute("facturas", facturas);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estadoFiltro", estado);

        return "private/mis_facturas";
    }

    // Descargar factura en PDF (método placeholder - necesitarás implementar la generación de PDF)
    @GetMapping("/descargar/{id}")
    public String descargarFactura(@PathVariable Long id, HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);
        Optional<Factura> facturaOpt = facturaService.buscarPorId(id);

        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();

            // Verificar que la factura pertenece al usuario
            if (!factura.getUsuario().getId().equals(usuario.getId())) {
                return "redirect:/private/mis-facturas";
            }

            // Aquí implementarías la lógica para generar y descargar el PDF
            // Por ahora solo redirige al detalle
            model.addAttribute("mensaje", "Función de descarga en desarrollo");
            return "redirect:/private/detalle/" + id;
        }

        return "redirect:/private/mis-facturas";
    }

    // ===== MÉTODOS PARA ADMINISTRADOR =====

    // Listar todas las facturas (solo admin)
    @GetMapping("/admin/todas")
    public String listarTodasFacturas(HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);

        // Verificar que sea administrador
        if (!"ADMINISTRADOR".equals(usuario.getRol())) {
            return "redirect:/private/mis-facturas";
        }

        List<Factura> facturas = facturaService.listarTodas();
        model.addAttribute("facturas", facturas);
        model.addAttribute("usuario", usuario);

        return "private/admin_facturas";
    }

    // Cambiar estado de factura (solo admin)
    @PostMapping("/admin/cambiar-estado")
    public String cambiarEstadoFactura(@RequestParam("idFactura") Long idFactura,
                                       @RequestParam("nuevoEstado") String nuevoEstado,
                                       HttpSession session) {
        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);

        // Verificar que sea administrador
        if (!"ADMINISTRADOR".equals(usuario.getRol())) {
            return "redirect:/private/mis-facturas";
        }

        facturaService.cambiarEstado(idFactura, nuevoEstado);

        return "redirect:/private/admin/todas";
    }

    // Anular factura (solo admin)
    @PostMapping("/admin/anular/{id}")
    public String anularFactura(@PathVariable Long id, HttpSession session) {
        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);

        // Verificar que sea administrador
        if (!"ADMINISTRADOR".equals(usuario.getRol())) {
            return "redirect:/private/mis-facturas";
        }

        facturaService.anularFactura(id);

        return "redirect:/private/admin/todas";
    }
}
