package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Pqrs;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import com.agrosellnova.Agrosellnova.servicio.ReservaService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.agrosellnova.Agrosellnova.repositorio.ReservaRepository;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/public/reservas")
    public String mostrarProductosReservables(HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        List<Producto> productos = productoService.obtenerProductosParaReserva();
        model.addAttribute("productos", productos);

        return "public/reservas";
    }

    @GetMapping("/formulario_reserva")
    public String mostrarFormularioReserva(Model model) {
        model.addAttribute("reserva", new Reserva());
        return "redirect:/forms/formulario_reserva";
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


    @GetMapping("/editar_reserva")
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
            String nombreUsuario = (String) session.getAttribute("usuario");
            if (nombreUsuario == null) {
                return "redirect:/public/index";
            }

            String nombreArchivo = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
            String rutaAbsoluta = new File("Agrosellnova/src/main/resources/static/img").getAbsolutePath();
            Files.createDirectories(Paths.get(rutaAbsoluta));
            Path path = Paths.get(rutaAbsoluta, nombreArchivo);
            Files.write(path, imagen.getBytes());
            String rutaRelativa = "../img/" + nombreArchivo;

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
    public void exportReservasToPdf(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reservas.pdf");

        List<Reserva> reserva = reservaService.obtenerTodasLasReservas();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(34,139,34));
        Paragraph title = new Paragraph("Lista de Reservas Registrados ",titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        Paragraph fecha = new Paragraph("Fecha de generación: " + new Date().toString());
        fecha.setAlignment(Element.ALIGN_RIGHT);
        document.add(fecha);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);

        float[] columnWidths = {1f, 2f, 1.5f, 2f, 2.5f, 1.5f, 1f, 2f, 2f};
        table.setWidths(columnWidths);
        int rowIndex = 0;
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        addCellToTable(table, "ID", headerFont, true, rowIndex);
        addCellToTable(table, "Usuario", headerFont, true, rowIndex);
        addCellToTable(table, "Documento", headerFont, true, rowIndex);
        addCellToTable(table, "Telefono", headerFont, true, rowIndex);
        addCellToTable(table, "Correo", headerFont, true, rowIndex);
        addCellToTable(table, "Producto", headerFont, true, rowIndex);
        addCellToTable(table, "Cantidad", headerFont, true, rowIndex);
        addCellToTable(table, "Metodo Pago", headerFont, true, rowIndex);
        addCellToTable(table, "Fecha Reserva", headerFont, true, rowIndex);

        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

        for (Reserva reservas: reserva) {
            addCellToTable(table, String.valueOf(reservas.getIdReserva()), dataFont, false, rowIndex);
            addCellToTable(table, reservas.getUsuarioCliente() != null ? reservas.getUsuarioCliente() : "", dataFont, false, rowIndex);
            addCellToTable(table, reservas.getUsuarioDocumento() != null ? reservas.getUsuarioDocumento() : "", dataFont, false, rowIndex);
            addCellToTable(table, reservas.getUsuarioTelefono() != null ? reservas.getUsuarioTelefono() : "", dataFont, false, rowIndex);
            addCellToTable(table, reservas.getUsuarioCorreo() != null ? reservas.getUsuarioCorreo() : "", dataFont, false, rowIndex);
            addCellToTable(table, reservas.getProducto() != null ? reservas.getProducto() : "", dataFont, false, rowIndex);
            addCellToTable(table, String.valueOf(reservas.getCantidadKg()), dataFont, false, rowIndex);
            addCellToTable(table, reservas.getMetodoPago() != null ? reservas.getMetodoPago() : "", dataFont, false, rowIndex);
            addCellToTable(table, reservas.getFechaReserva() != null ? reservas.getFechaReserva().toString() : "", dataFont, false, rowIndex);
            rowIndex++;
        }

        document.add(table);

        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Total de Reservas: " + reserva.size());
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


