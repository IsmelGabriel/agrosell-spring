package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.repositorio.UsuarioRepository;
import com.agrosellnova.Agrosellnova.servicio.EmailService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioServiceImpl;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.agrosellnova.Agrosellnova.modelo.Productor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.agrosellnova.Agrosellnova.servicio.ProductorService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/private")
public class ProductorController {

    @Autowired
    private ProductorService productorService;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EmailService emailService;

    private Long obtenerIdUsuario(HttpSession session, String nombreUsuario) {
        Long idUsuario = (Long) session.getAttribute("ID_USUARIO");

        if (idUsuario == null && nombreUsuario != null) {
            Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
            if (usuario != null) {
                idUsuario = usuario.getId();
                session.setAttribute("ID_USUARIO", idUsuario);
            }
        }
        return idUsuario;
    }

    @GetMapping("/ser_productor")
    public String mostrarFormularioProductor(HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");
        Long idUsuario = obtenerIdUsuario(session, usuario);

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        boolean tieneProductor = productorService.yaEsProductor(idUsuario.intValue());
        boolean puedeEnviar = productorService.puedeEnviarSolicitud(idUsuario.intValue());

        model.addAttribute("tieneProductor", tieneProductor);
        model.addAttribute("puedeEnviarSolicitud", puedeEnviar);

        if (tieneProductor) {
            Optional<Productor> productorExistente = productorService.obtenerPorUsuario(idUsuario.intValue());
            if (productorExistente.isPresent()) {
                Productor productor = productorExistente.get();
                model.addAttribute("productorExistente", productor);

                String mensaje = switch (productor.getEstadoSolicitud()) {
                    case Pendiente -> "Tu solicitud está siendo revisada.";
                    case Aprobado -> "¡Felicidades! Tu solicitud ha sido aprobada. Ya eres productor.";
                    case Rechazado -> "Tu solicitud ha sido rechazada. Puedes enviar una nueva solicitud con la información actualizada.";
                };
                model.addAttribute("mensajeEstado", mensaje);
            }
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        return "private/ser_productor";
    }

    @PostMapping("/ser_productor")
    public String procesarSolicitudProductor(
            @RequestParam("nombreFinca") String nombreFinca,
            @RequestParam("ubicacion") String ubicacion,
            @RequestParam(value = "areaCultivo", required = false) BigDecimal areaCultivo,
            @RequestParam("tipoProduccion") String tipoProduccion,
            @RequestParam(value = "anosExperiencia", required = false) Integer anosExperiencia,
            @RequestParam(value = "capacidadProduccion", required = false) BigDecimal capacidadProduccion,
            @RequestParam("contactoComercial") String contactoComercial,
            @RequestParam(value = "productos", required = false) String productos,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            String usuario = (String) session.getAttribute("usuario");
            String rol = (String) session.getAttribute("rol");
            Long idUsuario = obtenerIdUsuario(session, usuario);

            if (usuario == null || rol == null) {
                return "redirect:/public/index";
            }

            if (!productorService.puedeEnviarSolicitud(idUsuario.intValue())) {
                redirectAttributes.addFlashAttribute("error", "No puedes enviar una nueva solicitud en este momento");
                return "redirect:/private/ser_productor";
            }

            Productor nuevoProductor = new Productor();
            nuevoProductor.setIdUsuario(idUsuario.intValue());
            nuevoProductor.setNombreFinca(nombreFinca.trim());
            nuevoProductor.setUbicacion(ubicacion.trim());
            nuevoProductor.setAreaCultivo(areaCultivo);
            nuevoProductor.setTipoProduccion(Productor.TipoProduccion.valueOf(tipoProduccion));
            nuevoProductor.setAnosExperiencia(anosExperiencia);
            nuevoProductor.setCapacidadProduccion(capacidadProduccion);
            nuevoProductor.setContactoComercial(contactoComercial.trim());
            nuevoProductor.setProductos(productos != null ? productos.trim() : null);
            nuevoProductor.setDescripcion(descripcion != null ? descripcion.trim() : null);

            productorService.crearOActualizarSolicitudProductor(nuevoProductor);

            Usuario usuarioEntity = usuarioRepository.findById(Long.valueOf(idUsuario)).orElseThrow(
                    () -> new RuntimeException("Usuario no encontrado")
            );
            emailService.sendProducerApplicationEmail(usuarioEntity.getCorreo(), usuarioEntity.getNombreUsuario());

            redirectAttributes.addFlashAttribute("success", "¡Solicitud enviada exitosamente! Te contactaremos pronto.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
        }

        return "redirect:/private/ser_productor";
    }



    @GetMapping("/gestionar_productores")
    public String gestionarProductores(
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "buscar", required = false) String buscar,
            HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        if (!"administrador".equals(rol)) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return "redirect:/public/inicio";
        }

        List<Productor> productores;

        if (estado != null && !estado.isEmpty()) {
            try {
                Productor.EstadoSolicitud estadoEnum = Productor.EstadoSolicitud.valueOf(estado);
                productores = productorService.obtenerPorEstado(estadoEnum);
            } catch (IllegalArgumentException e) {
                productores = productorService.obtenerTodos();
            }
        }
        else if (buscar != null && !buscar.trim().isEmpty()) {
            String termino = buscar.trim();
            List<Productor> porFinca = productorService.buscarPorNombreFinca(termino);
            List<Productor> porUbicacion = productorService.buscarPorUbicacion(termino);
            List<Productor> porProductos = productorService.buscarPorProductos(termino);
            productores = new ArrayList<>(porFinca);
            for (Productor p : porUbicacion) {
                if (!productores.contains(p)) {
                    productores.add(p);
                }
            }
            for (Productor p : porProductos) {
                if (!productores.contains(p)) {
                    productores.add(p);
                }
            }
        }
        else {
            productores = productorService.obtenerTodos();
        }
        productores.sort((p1, p2) -> {
            if (p1.getEstadoSolicitud() == Productor.EstadoSolicitud.Pendiente &&
                    p2.getEstadoSolicitud() == Productor.EstadoSolicitud.Pendiente) {
                return p1.getFechaRegistro().compareTo(p2.getFechaRegistro());
            }
            if (p1.getEstadoSolicitud() == Productor.EstadoSolicitud.Pendiente) return -1;
            if (p2.getEstadoSolicitud() == Productor.EstadoSolicitud.Pendiente) return 1;

            return p2.getFechaRegistro().compareTo(p1.getFechaRegistro());
        });


        long pendientes = productorService.contarPorEstado(Productor.EstadoSolicitud.Pendiente);
        long aprobados = productorService.contarPorEstado(Productor.EstadoSolicitud.Aprobado);
        long rechazados = productorService.contarPorEstado(Productor.EstadoSolicitud.Rechazado);

        model.addAttribute("productores", productores);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("terminoBusqueda", buscar);
        model.addAttribute("cantidadPendientes", pendientes);
        model.addAttribute("cantidadAprobados", aprobados);
        model.addAttribute("cantidadRechazados", rechazados);
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);


        return "private/gestionar_productores";
    }

    @PostMapping("/aprobar_productor/{id}")
    public String aprobarProductor(@PathVariable Long id, @RequestParam("id_usuario") Long idUsuario,
                                   HttpSession session, RedirectAttributes redirectAttributes, Model model) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");
        model.addAttribute("usuario", usuario);

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        if (!"administrador".equals(rol)) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return "redirect:/public/inicio";
        }

        try {
            productorService.aprobarSolicitud(id);
            usuarioService.actualizarRol(idUsuario, "productor");

            Usuario usuarioEntity = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            emailService.sendAcceptedProducerEmail(usuarioEntity.getCorreo(), usuarioEntity.getNombreUsuario());
            redirectAttributes.addFlashAttribute("exito", "Solicitud aprobada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al aprobar solicitud: " + e.getMessage());
        }

        return "redirect:/private/gestionar_productores";
    }



    @PostMapping("/rechazar_productor/{id}")
    public String rechazarProductor(@PathVariable Long id,
                                    HttpSession session, RedirectAttributes redirectAttributes) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        if (!"administrador".equals(rol)) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado");
            return "redirect:/public/inicio";
        }

        try {
            // Obtener la solicitud para sacar el idUsuario
            Productor solicitud = productorService.obtenerPorId(id)
                    .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

            productorService.rechazarSolicitud(id);

            Usuario usuarioEntity = usuarioRepository.findById((long) solicitud.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            emailService.sendRejectedProducerEmail(usuarioEntity.getCorreo(), usuarioEntity.getNombreUsuario());
            redirectAttributes.addFlashAttribute("exito", "Solicitud rechazada");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al rechazar solicitud: " + e.getMessage());
        }

        return "redirect:/private/gestionar_productores";
    }


    @GetMapping("/ver_productor/{id}")
    public String verProductor(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        Optional<Productor> productorOpt = productorService.obtenerPorId(id);
        if (productorOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Productor no encontrado");
            return "redirect:/private/gestionar_productores";
        }

        model.addAttribute("productor", productorOpt.get());
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);

        return "ver_productor";
    }
    @GetMapping("/export/gestionar_productos")
    public void exportUsersToPdf(HttpServletResponse response,HttpSession session, Model model) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=productos.pdf");
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");
        Long idUsuario = obtenerIdUsuario(session, usuario);



        List<Producto> productos = productoService.obtenerProductosPorProductor(usuario);
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(34,139,34));
        Paragraph title = new Paragraph("Lista de Poductos Registrados ",titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        Paragraph fecha = new Paragraph("Fecha de generación: " + new Date().toString());
        fecha.setAlignment(Element.ALIGN_RIGHT);
        document.add(fecha);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);

        float[] columnWidths = {1f, 2f, 1.5f, 2f, 2.5f, 1.5f, 1.5f, 1f};
        table.setWidths(columnWidths);
        int rowIndex = 0;
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        addCellToTable(table, "ID", headerFont, true, rowIndex);
        addCellToTable(table, "Producto", headerFont, true, rowIndex);
        addCellToTable(table, "Descripcion", headerFont, true, rowIndex);
        addCellToTable(table, "Precio", headerFont, true, rowIndex);
        addCellToTable(table, "Peso Kg", headerFont, true, rowIndex);
        addCellToTable(table, "Stock", headerFont, true, rowIndex);
        addCellToTable(table, "Fecha Cos.", headerFont, true, rowIndex);
        addCellToTable(table, "Estado", headerFont, true, rowIndex);

        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

        for (Producto producto : productos) {
            addCellToTable(table, String.valueOf(producto.getId()), dataFont, false, rowIndex);
            addCellToTable(table, producto.getNombre() != null ? producto.getNombre() : "", dataFont, false, rowIndex);
            addCellToTable(table, producto.getDescripcion() != null ? producto.getDescripcion() : "", dataFont, false, rowIndex);
            addCellToTable(table, String.valueOf(producto.getPrecio()), dataFont, false, rowIndex);
            addCellToTable(table, String.valueOf(producto.getPesoKg()), dataFont, false, rowIndex);
            addCellToTable(table, String.valueOf(producto.getStock()), dataFont, false, rowIndex);
            addCellToTable(table, producto.getFechaCosecha() != null ? producto.getFechaCosecha().toString() : "", dataFont, false, rowIndex);
            addCellToTable(table, producto.getEstado() != null ? producto.getEstado() : "", dataFont, false, rowIndex);
            rowIndex++;
        }

        document.add(table);

        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Total de productos: " + productos.size());
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

}
