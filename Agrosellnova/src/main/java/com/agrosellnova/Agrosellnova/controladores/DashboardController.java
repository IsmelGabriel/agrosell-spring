package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.repositorio.VentaRepository;
import com.agrosellnova.Agrosellnova.servicio.DashboardService;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import com.agrosellnova.Agrosellnova.servicio.VentaService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;


@Controller
@RequestMapping
public class DashboardController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private DashboardService dashboardService;

    @RequestMapping("/private/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String rol= (String)  session.getAttribute("rol");
        String usuario = (String) session.getAttribute("usuario");

        // Top 4 ventas recientes
        List<Venta> ventasRecientes = ventaRepository.findTop4ByOrderByIdVentaDesc();

        // Estadísticas
        Double totalVentas = ventaRepository.totalVentas();
        Double totalProductos = ventaRepository.totalProductosVendidos();
        Long totalClientes = ventaRepository.totalClientes();

        // Añadir al modelo
        model.addAttribute("ventasRecientes", ventasRecientes);
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalVentas", totalVentas);
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("rol", rol);



        System.out.println("Total Ventas: " + totalVentas);
        System.out.println("Total Productos Vendidos: " + totalProductos);
        System.out.println("Total Clientes: " + totalClientes);

        return "private/dashboard";
    }

    @GetMapping("/dashboard/ventas-mensuales")
    @ResponseBody
    public List<Map<String, Object>> obtenerVentasMensuales() {
        List<Object[]> resultados = ventaService.obtenerVentasMensuales();
        List<Map<String, Object>> datos = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("mes", fila[0]);     // número de mes (1–12)
            map.put("total", fila[1]);   // total vendido
            datos.add(map);
        }
        return datos;
    }

    @GetMapping("/dashboard/productos-mas-vendidos")
    @ResponseBody
    public List<Map<String, Object>> obtenerProductosMasVendidos() {
        List<Object[]> resultados = ventaService.obtenerProductosMasVendidos();
        List<Map<String, Object>> datos = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("producto", fila[0]);
            map.put("cantidad", fila[1]);
            datos.add(map);
        }
        return datos;
    }

    @PostMapping("/private/export/reporte-completo")
    public void exportarReporteCompleto(
            @RequestParam("chart1") String chart1DataUrl,
            @RequestParam("chart2") String chart2DataUrl,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Reporte_Completo_AgroSell.pdf");

        try {
            // === Obtener datos del dashboard ===
            double totalVentas = dashboardService.obtenerTotalVentas();
            double totalProductos = dashboardService.obtenerTotalProductosVendidos();
            long totalClientes = dashboardService.obtenerTotalClientes();
            List<Venta> ventasRecientes = dashboardService.obtenerVentasRecientes();

            // === Crear documento PDF ===
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // --- ENCABEZADO ---
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph titulo = new Paragraph("📊 AgroSell - Reporte General del Dashboard", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph fecha = new Paragraph("Generado el: " + LocalDate.now());
            fecha.setAlignment(Element.ALIGN_CENTER);
            document.add(fecha);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // --- RESUMEN GENERAL ---
            Font seccionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
            document.add(new Paragraph("📈 RESUMEN GENERAL", seccionFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Ventas Totales: " + String.format("%,.2f $", totalVentas)));
            document.add(new Paragraph("Productos Vendidos: " + String.format("%,.2f kg", totalProductos)));
            document.add(new Paragraph("Clientes Totales: " + totalClientes));

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // --- TABLA DE ÓRDENES ---
            document.add(new Paragraph("🧾 ÓRDENES RECIENTES", seccionFont));
            document.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{1.2f, 2.5f, 2.5f, 1.5f, 2f});

            tabla.addCell("Orden");
            tabla.addCell("Cliente");
            tabla.addCell("Producto");
            tabla.addCell("Cantidad (Kg)");
            tabla.addCell("Total ($)");

            for (Venta venta : ventasRecientes) {
                tabla.addCell("#" + venta.getIdVenta());
                tabla.addCell(venta.getComprador().getNombre());
                tabla.addCell(venta.getProducto().getNombre());
                tabla.addCell(String.valueOf(venta.getCantidadKg()));
                tabla.addCell(String.format("%,.2f", venta.getTotalVenta()));
            }

            document.add(tabla);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // --- GRÁFICAS (desde base64) ---
            document.add(new Paragraph("📊 GRÁFICAS DEL DASHBOARD", seccionFont));
            document.add(new Paragraph(" "));

            Image chart1 = Image.getInstance(Base64.getDecoder().decode(chart1DataUrl.split(",")[1]));
            chart1.scaleToFit(480, 260);
            chart1.setAlignment(Element.ALIGN_CENTER);
            document.add(chart1);

            document.add(new Paragraph(" "));

            Image chart2 = Image.getInstance(Base64.getDecoder().decode(chart2DataUrl.split(",")[1]));
            chart2.scaleToFit(480, 260);
            chart2.setAlignment(Element.ALIGN_CENTER);
            document.add(chart2);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Reporte generado automáticamente por AgroSell",
                    FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY)));

            document.close();

        } catch (DocumentException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al generar el PDF completo: " + e.getMessage());
        }
    }


}
