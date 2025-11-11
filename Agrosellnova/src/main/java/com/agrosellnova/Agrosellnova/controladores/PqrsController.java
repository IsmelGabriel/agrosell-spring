package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Pqrs;
import com.agrosellnova.Agrosellnova.repositorio.PqrsRepository;
import com.agrosellnova.Agrosellnova.servicio.EmailService;
import com.agrosellnova.Agrosellnova.servicio.PqrsService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping()
public class PqrsController {

    @Autowired
    private PqrsService pqrsService;

    @Autowired
    private PqrsRepository pqrsRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/public/registrarPQRS")
    public String registrarPqrs(@ModelAttribute Pqrs pqrs, RedirectAttributes redirectAttrs, HttpSession session) {

        String nombreUsuario = (String) session.getAttribute("usuario");
        if (nombreUsuario == null) {
            session.setAttribute("pqrsTemp", pqrs);
            return "redirect:/public/index";
        }

        pqrs.setEstado(Pqrs.Estado.PENDIENTE);
        pqrs.setNombre(nombreUsuario);
        pqrsService.guardar(pqrs);
        redirectAttrs.addFlashAttribute("mensaje", "PQRS registrada correctamente");
        return "redirect:/public/pqrs_exitosa";
    }

    @GetMapping("/public/export/reporte_pqrs")
    public void exportPqrsToPdf(
            HttpSession session,
            HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=PQRSs.pdf");

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        List<Pqrs> pqrs;

        if (rol.equals("administrador")) {
            pqrs = pqrsService.listarTodas();
        } else {
            pqrs = pqrsService.obtenerPorUsuario(usuario);
        }

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

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        float[] columnWidths = {1f, 1.5f, 1.5f, 1f, 1.5f, 4f};
        table.setWidths(columnWidths);
        int rowIndex = 0;
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        addCellToTable(table, "Nombre", headerFont, true, rowIndex);
        addCellToTable(table, "Correo", headerFont, true, rowIndex);
        addCellToTable(table, "Telefono", headerFont, true, rowIndex);
        addCellToTable(table, "Tipo", headerFont, true, rowIndex);
        addCellToTable(table, "Estado", headerFont, true, rowIndex);
        addCellToTable(table, "Mensaje", headerFont, true, rowIndex);

        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

        for (Pqrs pqrss: pqrs) {
            addCellToTable(table, pqrss.getNombre() != null ? pqrss.getNombre() : "", dataFont, false, rowIndex);
            addCellToTable(table, pqrss.getCorreo() != null ? pqrss.getCorreo() : "", dataFont, false, rowIndex);
            addCellToTable(table, pqrss.getTelefono() != null ? pqrss.getTelefono() : "", dataFont, false, rowIndex);
            addCellToTable(table, pqrss.getTipo() != null ? pqrss.getTipo() : "", dataFont, false, rowIndex);
            addCellToTable(table, pqrss.getEstado() != null ? pqrss.getEstado().toString() : "", dataFont, false, rowIndex);
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

    @PostMapping("/public/private/gestionar_pqrs/responder/{idPqrs}")
    @ResponseBody
    public ResponseEntity<?> responderPqrs(
            @PathVariable Long idPqrs,
            @RequestParam("respuesta") String respuesta) {

        Pqrs pqrs = pqrsService.obtenerPorId(idPqrs);

        if (pqrs == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "PQRS no encontrada"));
        }

        // Cambiar el estado a RESUELTO
        pqrs.setEstado(Pqrs.Estado.RESUELTO);
        pqrs.setRespuesta(respuesta);
        pqrsService.guardar(pqrs);

        try {
            emailService.sendResponsePqrsEmail(pqrs.getCorreo(), pqrs.getNombre(), respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "PQRS resuelta pero falló el envío del correo."));
        }

        return ResponseEntity.ok(Map.of("mensaje", "PQRS resuelta y correo enviado correctamente"));
    }

    @GetMapping("/private/gestionar_pqrs")
    public String gestionarPqrs(Model model, HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null) {
            return "redirect:/public/index";
        }

        // Obtener PQRS solo del usuario actual
        List<Pqrs> pqrsList = pqrsService.obtenerPorUsuario(usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("pqrsList", pqrsList);

        return "private/gestionar_pqrs";
    }


}
