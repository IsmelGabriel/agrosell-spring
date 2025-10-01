package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.servicio.VentaService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/private")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping("/gestionar_ventas")
    public String gestionarVentas(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            HttpSession session,
            Model model) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null || !rol.equals("productor")) {
            return "redirect:/public/index";
        }

        List<Venta> listaVentas;
        if (criterio != null && valor != null && !valor.isBlank()) {
            listaVentas = ventaService.filtrarVentas(usuario, criterio, valor);
        } else {
            listaVentas = ventaService.obtenerVentasPorProductor(usuario);
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("listaVentas", listaVentas);

        return "private/gestionar_ventas";
    }

    @GetMapping("/gestionar_compra")
    public String mostrarComprasCliente(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            HttpSession session,
            Model model) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null || !rol.equals("cliente")) {
            return "redirect:/public/index";
        }

        List<Venta> listaCompra;
        if (criterio != null && valor != null && !valor.isBlank()) {
            listaCompra = ventaService.filtrarCompras(usuario, criterio, valor);
        } else {
            listaCompra = ventaService.findByComprador_NombreUsuario(usuario);
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("listaCompra", listaCompra);

        return "private/gestionar_compra";
    }

    @GetMapping("/export/gestionar_ventas")
    public void exportUsersToPdf(HttpServletResponse response, HttpSession session) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Ventas.pdf");
        String usuario = (String) session.getAttribute("usuario");


        List<Venta> ventas = ventaService.obtenerVentasPorProductor(usuario);
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(34,139,34));
        Paragraph title = new Paragraph("Lista de Ventas Realizadas",titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        Paragraph fecha = new Paragraph("Fecha de generación: " + new Date().toString());
        fecha.setAlignment(Element.ALIGN_RIGHT);
        document.add(fecha);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        float[] columnWidths = {1f, 2f, 1.5f, 2f, 2.5f, 1.5f, 1.5f};
        table.setWidths(columnWidths);
        int rowIndex = 0;
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        addCellToTable(table, "ID", headerFont, true, rowIndex);
        addCellToTable(table, "Producto", headerFont, true, rowIndex);
        addCellToTable(table, "Cantidad", headerFont, true, rowIndex);
        addCellToTable(table, "Precio Und", headerFont, true, rowIndex);
        addCellToTable(table, "Total venta", headerFont, true, rowIndex);
        addCellToTable(table, "Comprador", headerFont, true, rowIndex);
        addCellToTable(table, "Fecha ", headerFont, true, rowIndex);


        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

        for (Venta venta : ventas ) {
            addCellToTable(table, venta.getIdVenta() != null ? String.valueOf(venta.getIdVenta()) : "", dataFont, false, rowIndex);
            addCellToTable(table, (venta.getProducto() != null && venta.getProducto().getNombre() != null) ? venta.getProducto().getNombre() : "", dataFont, false, rowIndex);
            addCellToTable(table, venta.getCantidadKg() != null ? String.valueOf(venta.getCantidadKg()) : "", dataFont, false, rowIndex);
            addCellToTable(table, String.valueOf(venta.getProducto().getPrecio()), dataFont, false, rowIndex);
            addCellToTable(table, venta.getTotalVenta() != null ? String.valueOf(venta.getTotalVenta()) : "", dataFont, false, rowIndex);
            addCellToTable(table, (venta.getComprador() != null && venta.getComprador().getNombreUsuario() != null) ? venta.getComprador().getNombreUsuario() : "", dataFont, false, rowIndex);
            addCellToTable(table, venta.getFechaVenta() != null ? venta.getFechaVenta().toString() : "", dataFont, false,rowIndex);
            rowIndex++;
        }

        document.add(table);

        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Total de ventas: " + ventas.size());
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

    @GetMapping("/export/reporte_ventas")
    public void exportVentasToPdf(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ventas.pdf");

        List<Venta> ventas = ventaService.obtenerTodasLasVentas();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(34,139,34));
        Paragraph title = new Paragraph("Lista de Ventas Registrados ",titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        Paragraph fecha = new Paragraph("Fecha de generación: " + new Date().toString());
        fecha.setAlignment(Element.ALIGN_RIGHT);
        document.add(fecha);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        float[] columnWidths = {1f, 2f, 1.5f, 2f, 2.5f, 1.5f, 1.5f};
        table.setWidths(columnWidths);
        int rowIndex = 0;
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        addCellToTable(table, "ID", headerFont, true, rowIndex);
        addCellToTable(table, "Comprador", headerFont, true, rowIndex);
        addCellToTable(table, "Vendedor", headerFont, true, rowIndex);
        addCellToTable(table, "Producto", headerFont, true, rowIndex);
        addCellToTable(table, "Cantidad", headerFont, true, rowIndex);
        addCellToTable(table, "Total", headerFont, true, rowIndex);
        addCellToTable(table, "Fecha venta", headerFont, true, rowIndex);

        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Venta venta : ventas) {
            addCellToTable(table, String.valueOf(venta.getIdVenta()), dataFont, false, rowIndex);
            addCellToTable(table, (venta.getComprador() != null && venta.getComprador().getNombreUsuario() != null) ? venta.getComprador().getNombreUsuario() : "", dataFont, false, rowIndex);
            addCellToTable(table, (venta.getVendedor() != null && venta.getVendedor().getNombreUsuario() != null) ? venta.getVendedor().getNombreUsuario() : "", dataFont, false, rowIndex);
            addCellToTable(table, (venta.getProducto() != null && venta.getProducto().getNombre() != null) ? venta.getProducto().getNombre() : "", dataFont, false, rowIndex);
            addCellToTable(table, venta.getCantidadKg() != null ? String.valueOf(venta.getCantidadKg()) : "", dataFont, false, rowIndex);
            addCellToTable(table, venta.getTotalVenta() != null ? String.valueOf(venta.getTotalVenta()) : "", dataFont, false, rowIndex);
            addCellToTable(table, venta.getFechaVenta() != null ? venta.getFechaVenta().format(formatter) : "", dataFont, false, rowIndex);
            rowIndex++;
        }

        document.add(table);

        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Total de Ventas: " + ventas.size());
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    }

    @GetMapping("/export/gestionar_compra")
    public void exportGestionarCompraToPdf(HttpSession session, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Gestionar_compra.pdf");

        String usuario = (String) session.getAttribute("usuario");

        List<Venta> ventas = ventaService.findByComprador_NombreUsuario(usuario);

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(34,139,34));
        Paragraph title = new Paragraph("Lista de Ventas Registrados ",titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        Paragraph fecha = new Paragraph("Fecha de generación: " + new Date().toString());
        fecha.setAlignment(Element.ALIGN_RIGHT);
        document.add(fecha);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        float[] columnWidths = {1f, 2f, 1.5f, 2f, 2.5f, 1.5f};
        table.setWidths(columnWidths);
        int rowIndex = 0;
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        addCellToTable(table, "ID", headerFont, true, rowIndex);
        addCellToTable(table, "Vendedor", headerFont, true, rowIndex);
        addCellToTable(table, "Producto", headerFont, true, rowIndex);
        addCellToTable(table, "Cantidad", headerFont, true, rowIndex);
        addCellToTable(table, "Total", headerFont, true, rowIndex);
        addCellToTable(table, "Fecha venta", headerFont, true, rowIndex);

        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Venta venta : ventas) {
            addCellToTable(table, String.valueOf(venta.getIdVenta()), dataFont, false, rowIndex);
            addCellToTable(table, (venta.getVendedor() != null && venta.getVendedor().getNombreUsuario() != null) ? venta.getVendedor().getNombreUsuario() : "", dataFont, false, rowIndex);
            addCellToTable(table, (venta.getProducto() != null && venta.getProducto().getNombre() != null) ? venta.getProducto().getNombre() : "", dataFont, false, rowIndex);
            addCellToTable(table, venta.getCantidadKg() != null ? String.valueOf(venta.getCantidadKg()) : "", dataFont, false, rowIndex);
            addCellToTable(table, venta.getTotalVenta() != null ? String.valueOf(venta.getTotalVenta()) : "", dataFont, false, rowIndex);
            addCellToTable(table, venta.getFechaVenta() != null ? venta.getFechaVenta().format(formatter) : "", dataFont, false, rowIndex);
            rowIndex++;
        }

        document.add(table);

        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Total de compras: " + ventas.size());
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    }
}
