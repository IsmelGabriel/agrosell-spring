package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Pqrs;
import com.agrosellnova.Agrosellnova.servicio.PqrsService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/public")
public class PqrsController {

    @Autowired
    private PqrsService pqrsService;

    @PostMapping("/registrarPQRS")
    public String registrarPqrs(@ModelAttribute Pqrs pqrs, RedirectAttributes redirectAttrs) {
        pqrsService.guardar(pqrs);
        redirectAttrs.addFlashAttribute("mensaje", "PQRS registrada correctamente");
        return "redirect:/public/ayuda";
    }

    @GetMapping("/export/reporte_pqrs")
    public void exportExportPqrsToPdf(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=PQRSs.pdf");

        List<Pqrs> pqrs = pqrsService.listarTodas();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(34,139,34));
        Paragraph title = new Paragraph("Lista de PQRS's Registrados ",titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        Paragraph fecha = new Paragraph("Fecha de generación: " + new Date().toString());
        fecha.setAlignment(Element.ALIGN_RIGHT);
        document.add(fecha);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        float[] columnWidths = {1f, 2f, 1.5f, 2f, 2.5f};
        table.setWidths(columnWidths);
        int rowIndex = 0;
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        addCellToTable(table, "ID", headerFont, true, rowIndex);
        addCellToTable(table, "Nombre", headerFont, true, rowIndex);
        addCellToTable(table, "Correo", headerFont, true, rowIndex);
        addCellToTable(table, "Telefono", headerFont, true, rowIndex);
        addCellToTable(table, "Mensaje", headerFont, true, rowIndex);

        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

        for (Pqrs pqrss: pqrs) {
            addCellToTable(table, String.valueOf(pqrss.getIdPqrs()), dataFont, false, rowIndex);
            addCellToTable(table, pqrss.getNombre() != null ? pqrss.getNombre() : "", dataFont, false, rowIndex);
            addCellToTable(table, pqrss.getCorreo() != null ? pqrss.getCorreo() : "", dataFont, false, rowIndex);
            addCellToTable(table, pqrss.getTelefono() != null ? pqrss.getTelefono() : "", dataFont, false, rowIndex);
            addCellToTable(table, pqrss.getMensaje() != null ? pqrss.getMensaje() : "", dataFont, false, rowIndex);

            rowIndex++;
        }

        document.add(table);

        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Total de PQRS's: " + pqrs.size());
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
