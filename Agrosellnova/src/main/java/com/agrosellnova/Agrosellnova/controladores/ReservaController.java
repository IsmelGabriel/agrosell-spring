package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import com.agrosellnova.Agrosellnova.servicio.ReservaService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.agrosellnova.Agrosellnova.repositorio.ReservaRepository;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;


@Controller
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/public/reservas")
    public String mostrarProductosReservables(HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        List<Producto> productos = productoService.obtenerProductosParaReserva();
        model.addAttribute("productos", productos);

        return "public/reservas";
    }

    @GetMapping("/formulario_reserva")
    public String mostrarFormularioReserva(@RequestParam("id") Long idProducto,
                                           Model model,
                                           HttpSession session,
                                           RedirectAttributes redirectAttributes) {
        // Verificar si hay usuario en sesión
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para reservar un producto.");
            return "redirect:/public/index"; // redirige al login
        }

        // Buscar el producto
        Producto producto = productoService.obtenerPorId(idProducto);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado con id: " + idProducto);
        }

        // Crear la reserva y rellenar datos del usuario si están disponibles
        Reserva reserva = new Reserva();
        reserva.setUsuarioCliente(usuario);

        // Si tienes una entidad Usuario en BD, puedes cargar más datos:
        Usuario usuarioEntidad = usuarioService.buscarPorNombreUsuario(usuario);
        if (usuarioEntidad != null) {
            reserva.setUsuarioCorreo(usuarioEntidad.getCorreo());
            reserva.setUsuarioDocumento(usuarioEntidad.getDocumento());
        }

        // Pasar al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("reserva", reserva);
        model.addAttribute("producto", producto);

        return "/forms/formulario_reserva";
    }



    @PostMapping("/public/registrarReserva")
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

    @GetMapping("/private/gestionar_reservas")
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


    @GetMapping("private/editar_reserva")
    public String mostrarFormularioEdicion(@RequestParam("id") Long id, HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));

        model.addAttribute("reserva", reserva);
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);

        return "forms/editar_reserva";
    }


    @GetMapping("/cancelar_reserva")
    public String cancelarReserva(@RequestParam("id") Long id, HttpSession session) {
        System.out.println("Cancelando reserva con ID: " + id);
        if (session.getAttribute("usuario") == null) {
            return "redirect:/public/index";
        }

        reservaRepository.deleteById(id);
        return "redirect:/private/gestionar_reservas";
    }

    @PostMapping("/private/guardarProductoReserva")
    public String guardarProductoReserva(
            @RequestParam("productoImagen") MultipartFile imagen,
            @RequestParam("nombreProducto") String nombre,
            @RequestParam("precio") double precio,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("pesoKg") double pesoKg,
            @RequestParam("stock") int stock,
            @RequestParam("fechaCosecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCosecha,
            HttpSession session
    ) {
        try {
            // 📌 Obtener usuario de la sesión
            String nombreUsuario = (String) session.getAttribute("usuario");
            if (nombreUsuario == null) {
                return "redirect:/public/index";
            }

            // 📂 Guardar la imagen
            String nombreArchivo = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
            String rutaAbsoluta = new File("Agrosellnova/src/main/resources/static/img").getAbsolutePath();
            Files.createDirectories(Paths.get(rutaAbsoluta));
            Path path = Paths.get(rutaAbsoluta, nombreArchivo);
            Files.write(path, imagen.getBytes());
            String rutaRelativa = "../img/" + nombreArchivo;

            // 📝 Crear el producto
            Producto producto = new Producto();
            producto.setUsuarioCampesino(nombreUsuario);
            producto.setImagen(rutaRelativa);
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setDescripcion(descripcion);
            producto.setPesoKg(pesoKg);
            producto.setStock(stock);
            producto.setEstado("Proximo a salir");
            producto.setFechaCosecha(fechaCosecha);

            productoRepository.save(producto);

            return "redirect:/private/gestionar_productos?success";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }
    @GetMapping("/export/reporte_reservas")
    public void exportReservasToPdf(HttpServletResponse response, HttpSession session)
            throws IOException, DocumentException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reservas.pdf");

        // 📌 Obtener usuario logueado desde sesión
        String documentoUsuario = (String) session.getAttribute("usuarioDocumento");

        // 📌 Consultar reservas
        List<Reserva> reservas = reservaService.obtenerTodasLasReservas();

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // 📌 Título
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(0, 102, 204));
        Paragraph title = new Paragraph("Reporte de Reservas del Usuario", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // 📌 Fecha de generación
        Paragraph fecha = new Paragraph("Fecha de generación: " + new Date().toString());
        fecha.setAlignment(Element.ALIGN_RIGHT);
        document.add(fecha);
        document.add(new Paragraph(" "));

        // 📌 Tabla con columnas
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);

        float[] columnWidths = {1f, 2f, 2f, 3f, 2.5f, 1.5f, 2f, 2f, 2f};
        table.setWidths(columnWidths);

        int rowIndex = 0;
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        addCellToTable(table, "ID", headerFont, true, rowIndex);
        addCellToTable(table, "Usuario", headerFont, true, rowIndex);
        addCellToTable(table, "Documento", headerFont, true, rowIndex);
        addCellToTable(table, "Teléfono", headerFont, true, rowIndex);
        addCellToTable(table, "Correo", headerFont, true, rowIndex);
        addCellToTable(table, "Producto", headerFont, true, rowIndex);
        addCellToTable(table, "Cantidad", headerFont, true, rowIndex);
        addCellToTable(table, "Método Pago", headerFont, true, rowIndex);
        addCellToTable(table, "Fecha Reser", headerFont, true, rowIndex);

        // 📌 Datos
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Reserva reserva : reservas) {
            addCellToTable(table, String.valueOf(reserva.getIdReserva()), dataFont, false, rowIndex);
            addCellToTable(table, reserva.getUsuarioCliente() != null ? reserva.getUsuarioCliente() : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getUsuarioDocumento() != null ? reserva.getUsuarioDocumento() : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getUsuarioTelefono() != null ? reserva.getUsuarioTelefono() : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getUsuarioCorreo() != null ? reserva.getUsuarioCorreo() : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getProducto() != null ? reserva.getProducto() : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getCantidadKg() != null ? String.valueOf(reserva.getCantidadKg()) : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getMetodoPago() != null ? reserva.getMetodoPago() : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getFechaReserva() != null ? reserva.getFechaReserva().format(formatter) : "", dataFont, false, rowIndex);
            rowIndex++;
        }

        document.add(table);

        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Total de Reservas: " + reservas.size());
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    }

    @GetMapping("/export/gestionar_reservas")
    public void exportGestionarReservasToPdf(HttpServletResponse response, HttpSession session)
            throws IOException, DocumentException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=gestionar_reservas.pdf");

        // 📌 Obtener usuario logueado desde sesión
        String usuario = (String) session.getAttribute("usuario");

        // 📌 Consultar reservas del usuario
        List<Reserva> reservas = reservaService.obtenerReservasPorUsuario(usuario);

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // 📌 Título
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(34,139,34));
        Paragraph title = new Paragraph("Lista de RESERVAs Registrados",titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // 📌 Fecha de generación
        Paragraph fecha = new Paragraph("Fecha de generación: " + new Date().toString());
        fecha.setAlignment(Element.ALIGN_RIGHT);
        document.add(fecha);
        document.add(new Paragraph(" "));

        // 📌 Tabla con columnas
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);

        float[] columnWidths = {1f, 2f, 2f, 3f, 2.5f, 1.5f, 2f, 2f};
        table.setWidths(columnWidths);

        int rowIndex = 0;
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        addCellToTable(table, "ID", headerFont, true, rowIndex);
        addCellToTable(table, "Documento", headerFont, true, rowIndex);
        addCellToTable(table, "Teléfono", headerFont, true, rowIndex);
        addCellToTable(table, "Correo", headerFont, true, rowIndex);
        addCellToTable(table, "Producto", headerFont, true, rowIndex);
        addCellToTable(table, "Cantidad", headerFont, true, rowIndex);
        addCellToTable(table, "Método Pago", headerFont, true, rowIndex);
        addCellToTable(table, "Fecha Reser", headerFont, true, rowIndex);

        // 📌 Datos
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Reserva reserva : reservas) {
            addCellToTable(table, String.valueOf(reserva.getIdReserva()), dataFont, false, rowIndex);
            addCellToTable(table, reserva.getUsuarioDocumento() != null ? reserva.getUsuarioDocumento() : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getUsuarioTelefono() != null ? reserva.getUsuarioTelefono() : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getUsuarioCorreo() != null ? reserva.getUsuarioCorreo() : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getProducto() != null ? reserva.getProducto() : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getCantidadKg() != null ? String.valueOf(reserva.getCantidadKg()) : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getMetodoPago() != null ? reserva.getMetodoPago() : "", dataFont, false, rowIndex);
            addCellToTable(table, reserva.getFechaReserva() != null ? reserva.getFechaReserva().format(formatter) : "", dataFont, false, rowIndex);
            rowIndex++;
        }

        document.add(table);
        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Total de Reservas: " + reservas.size());
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



