package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.repositorio.UsuarioRepository;
import com.agrosellnova.Agrosellnova.servicio.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.agrosellnova.Agrosellnova.modelo.Pqrs;
import com.agrosellnova.Agrosellnova.repositorio.PqrsRepository;


import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class AdministradorController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private PqrsService pqrsService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoService productoService;


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
                case "estado":
                    usuarios = usuarioRepository.findByEstadoContainingIgnoreCase(valor);
                    break;
                default:
                    usuarios = usuarioService.obtenerTodosLosUsuarios();
            }
        } else {
            usuarios = usuarioService.obtenerTodosLosUsuariosOrdenPorIdDesc();
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("rol", session.getAttribute("rol"));

        return "private/usuarios_registrados";
    }

    @GetMapping("/export/usuarios_registrados")
    public void exportUsersToPdf(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.pdf");

        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(34,139,34));
        Paragraph title = new Paragraph("Lista de Usuarios Registrados ",titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        Paragraph fecha = new Paragraph("Fecha de generación: " + new Date().toString());
        fecha.setAlignment(Element.ALIGN_RIGHT);
        document.add(fecha);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);

        float[] columnWidths = {1f, 2f, 1.5f, 2f, 2.5f, 1.5f, 1.5f, 1f, 1f};
        table.setWidths(columnWidths);
        int rowIndex = 0;
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        addCellToTable(table, "ID", headerFont, true, rowIndex);
        addCellToTable(table, "Nombre", headerFont, true, rowIndex);
        addCellToTable(table, "Documento", headerFont, true, rowIndex);
        addCellToTable(table, "Ubicación", headerFont, true, rowIndex);
        addCellToTable(table, "Correo", headerFont, true, rowIndex);
        addCellToTable(table, "Método Pago", headerFont, true, rowIndex);
        addCellToTable(table, "Fecha Nac.", headerFont, true, rowIndex);
        addCellToTable(table, "Rol", headerFont, true, rowIndex);
        addCellToTable(table, "Estado", headerFont, true, rowIndex);

        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

        for (Usuario usuario : usuarios) {
            addCellToTable(table, String.valueOf(usuario.getId()), dataFont, false, rowIndex);
            addCellToTable(table, usuario.getNombre() != null ? usuario.getNombre() : "", dataFont, false, rowIndex);
            addCellToTable(table, usuario.getDocumento() != null ? usuario.getDocumento() : "", dataFont, false, rowIndex);
            addCellToTable(table, usuario.getDireccion() != null ? usuario.getDireccion() : "", dataFont, false, rowIndex);
            addCellToTable(table, usuario.getCorreo() != null ? usuario.getCorreo() : "", dataFont, false, rowIndex);
            addCellToTable(table, usuario.getMetodoPago() != null ? usuario.getMetodoPago() : "", dataFont, false, rowIndex);
            addCellToTable(table, usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento().toString() : "", dataFont, false, rowIndex);
            addCellToTable(table, usuario.getRol() != null ? usuario.getRol() : "", dataFont, false, rowIndex);
            addCellToTable(table, usuario.getEstado() != null ? usuario.getEstado() : "", dataFont, false, rowIndex);
            rowIndex++;
        }

        document.add(table);

        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Total de usuarios: " + usuarios.size());
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    }
    private void addCellToTable(PdfPTable table, String content, Font font, boolean isHeader, int rowIndex) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        if (isHeader) {
            cell.setBackgroundColor(new BaseColor(200, 230, 200)); // verde claro
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderColor(new BaseColor(180, 220, 180)); // bordes verdes suaves
        } else {
            if (rowIndex % 2 == 0) {
                cell.setBackgroundColor(new BaseColor(235, 250, 235)); // verde muy suave
            }
            cell.setBorderColor(new BaseColor(220, 240, 220));

            cell.setBorderColor(new BaseColor(220, 240, 220)); // bordes más claros para datos
        }
        cell.setPadding(5);
        table.addCell(cell);
    }


    @GetMapping("/private/actualizarEstado")
    public String actualizarEstado(@RequestParam("idUsuario") Long idUsuario,
                                   @RequestParam("estado") String estado,
                                   HttpSession session,
                                   Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/public/index";
        }

        model.addAttribute("usuario", session.getAttribute("usuario"));

        // Llamada correcta al servicio
        usuarioService.actualizarEstado(idUsuario, estado);

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
            usuarios = usuarioService.obtenerTodosLosUsuariosOrdenPorIdDesc();
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
            if (criterio.equals("estado") && valor.toUpperCase().startsWith("R")) {
                valor = "RESUELTO";
            }else if (criterio.equals("estado") && valor.toUpperCase().startsWith("P")) {
                valor = "PENDIENTE";
            }

            lista = switch (criterio) {
                case "id" -> pqrsRepository.findAllByIdPqrs(Long.parseLong(valor));
                case "usuario" -> pqrsRepository.findByNombre(valor);
                case "correo" -> pqrsRepository.findByCorreoContainingIgnoreCase(valor);
                case "telefono" -> pqrsRepository.findByTelefonoContainingIgnoreCase(valor);
                case "estado" -> pqrsRepository.findByEstado(Pqrs.Estado.valueOf(valor));
                default -> pqrsRepository.findAll();
            };
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
