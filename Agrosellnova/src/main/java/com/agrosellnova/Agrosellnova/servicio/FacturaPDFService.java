package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.DetalleFactura;
import com.agrosellnova.Agrosellnova.modelo.Factura;
import com.agrosellnova.Agrosellnova.modelo.Pago;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class FacturaPDFService {

    public byte[] generarFacturaPdf(Factura factura) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();

        // Configurar fuentes
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font subtituloFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);
        Font normalBoldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

        // Título
        Paragraph titulo = new Paragraph("FACTURA", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        // Número de factura
        Paragraph numeroFactura = new Paragraph("N°: " +
                (factura.getNumeroFactura() != null ? factura.getNumeroFactura() : "N/A"),
                subtituloFont);
        numeroFactura.setAlignment(Element.ALIGN_RIGHT);
        numeroFactura.setSpacingAfter(15);
        document.add(numeroFactura);

        // Información en dos columnas
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(10);
        infoTable.setSpacingAfter(20);

        // Columna izquierda: Información del cliente
        PdfPCell celdaCliente = new PdfPCell();
        celdaCliente.setBorder(Rectangle.NO_BORDER);
        celdaCliente.setPadding(5);

        Paragraph clienteTitulo = new Paragraph("INFORMACIÓN DEL CLIENTE", subtituloFont);
        clienteTitulo.setSpacingAfter(5);
        celdaCliente.addElement(clienteTitulo);

        // Usar valores por defecto si son null
        Usuario usuario = factura.getUsuario();
        Pago pago = factura.getPago();

        celdaCliente.addElement(new Paragraph("Nombre: " +
                (usuario != null ? usuario.getNombre() : "N/A"), normalFont));
        celdaCliente.addElement(new Paragraph("Documento: " +
                (usuario != null ? usuario.getDocumento() : "N/A"), normalFont));
        celdaCliente.addElement(new Paragraph("Correo: " +
                (usuario != null ? usuario.getCorreo() : "N/A"), normalFont));
        celdaCliente.addElement(new Paragraph("Dirección: " +
                (pago != null ? pago.getDireccion() : "N/A"), normalFont));
        celdaCliente.addElement(new Paragraph("Teléfono: " +
                (pago != null ? pago.getTelefono() : "N/A"), normalFont));

        infoTable.addCell(celdaCliente);

        // Columna derecha: Información de la factura
        PdfPCell celdaFactura = new PdfPCell();
        celdaFactura.setBorder(Rectangle.NO_BORDER);
        celdaFactura.setPadding(5);

        Paragraph facturaTitulo = new Paragraph("INFORMACIÓN DE LA FACTURA", subtituloFont);
        facturaTitulo.setSpacingAfter(5);
        celdaFactura.addElement(facturaTitulo);

        // Formatear fecha de manera segura
        String fechaFormateada = "N/A";
        if (factura.getFechaEmision() != null) {
            try {
                fechaFormateada = new SimpleDateFormat("dd/MM/yyyy").format(factura.getFechaEmision());
            } catch (Exception e) {
                fechaFormateada = "Fecha inválida";
            }
        }

        celdaFactura.addElement(new Paragraph("Fecha emisión: " + fechaFormateada, normalFont));
        celdaFactura.addElement(new Paragraph("Método pago: " +
                (pago != null ? pago.getMetodoPago() : "N/A"), normalFont));

        // Estado
        String estado = factura.getEstadoFactura() != null ? factura.getEstadoFactura() : "PENDIENTE";
        Paragraph estadoParrafo = new Paragraph("Estado: " + estado, normalFont);
        BaseColor estadoColor = obtenerColorEstado(estado);
        if (estadoColor != null) {
            estadoParrafo.getFont().setColor(estadoColor);
        }
        celdaFactura.addElement(estadoParrafo);

        infoTable.addCell(celdaFactura);

        document.add(infoTable);

        // Línea separadora
        agregarLineaSeparadora(document);

        // Tabla de productos
        Paragraph productosTitulo = new Paragraph("DETALLE DE PRODUCTOS", subtituloFont);
        productosTitulo.setSpacingAfter(10);
        document.add(productosTitulo);

        PdfPTable productosTable = new PdfPTable(5);
        productosTable.setWidthPercentage(100);
        productosTable.setSpacingBefore(10);
        productosTable.setSpacingAfter(20);

        // Encabezados
        String[] headers = {"#", "Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Paragraph(header, normalBoldFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setBackgroundColor(new BaseColor(232, 245, 233));
            headerCell.setPadding(8);
            productosTable.addCell(headerCell);
        }

        // Filas de productos
        int contador = 1;
        if (factura.getDetalles() != null && !factura.getDetalles().isEmpty()) {
            for (DetalleFactura detalle : factura.getDetalles()) {
                productosTable.addCell(new PdfPCell(new Paragraph(String.valueOf(contador++), normalFont)));
                productosTable.addCell(new PdfPCell(new Paragraph(
                        detalle.getNombreProducto() != null ? detalle.getNombreProducto() : "N/A", normalFont)));
                productosTable.addCell(new PdfPCell(new Paragraph(
                        String.format("%.2f", detalle.getCantidad() != null ? detalle.getCantidad() : 0.0), normalFont)));
                productosTable.addCell(new PdfPCell(new Paragraph(
                        "$" + String.format("%,.2f", detalle.getPrecioUnitario() != null ? detalle.getPrecioUnitario() : 0.0), normalFont)));
                productosTable.addCell(new PdfPCell(new Paragraph(
                        "$" + String.format("%,.2f", detalle.getSubtotal() != null ? detalle.getSubtotal() : 0.0), normalFont)));
            }
        } else {
            PdfPCell vacioCell = new PdfPCell(new Paragraph("No hay productos", normalFont));
            vacioCell.setColspan(5);
            vacioCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            productosTable.addCell(vacioCell);
        }

        document.add(productosTable);

        // Línea separadora
        agregarLineaSeparadora(document);

        // Totales
        PdfPTable totalesTable = new PdfPTable(2);
        totalesTable.setWidthPercentage(50);
        totalesTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalesTable.setSpacingBefore(10);

        // Subtotal
        agregarFilaTotal(totalesTable, "Subtotal:",
                factura.getSubtotal() != null ? factura.getSubtotal() : 0.0,
                normalFont, normalFont);

        // Descuento
        if (factura.getDescuento() != null && factura.getDescuento() > 0) {
            agregarFilaTotal(totalesTable, "Descuento:", -factura.getDescuento(), normalFont, normalFont);
        }

        // Impuesto
        if (factura.getImpuesto() != null && factura.getImpuesto() > 0) {
            agregarFilaTotal(totalesTable, "Impuesto:", factura.getImpuesto(), normalFont, normalFont);
        }

        // TOTAL
        agregarFilaTotal(totalesTable, "TOTAL:",
                factura.getTotal() != null ? factura.getTotal() : 0.0,
                new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(46, 125, 50)),
                new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(46, 125, 50)));

        document.add(totalesTable);

        // Footer
        agregarFooter(document, normalFont);

        document.close();
        writer.close();

        return baos.toByteArray();
    }

    private void agregarLineaSeparadora(Document document) throws DocumentException {
        PdfPTable lineTable = new PdfPTable(1);
        lineTable.setWidthPercentage(100);
        lineTable.setSpacingBefore(10);
        lineTable.setSpacingAfter(10);

        PdfPCell lineCell = new PdfPCell();
        lineCell.setBorder(Rectangle.BOTTOM);
        lineCell.setBorderColor(new BaseColor(46, 125, 50));
        lineCell.setBorderWidth(2);
        lineCell.setFixedHeight(1);
        lineCell.setPadding(0);

        lineTable.addCell(lineCell);
        document.add(lineTable);
    }

    private void agregarFilaTotal(PdfPTable table, String concepto, Double valor,
                                  Font fontConcepto, Font fontValor) {
        PdfPCell cellConcepto = new PdfPCell(new Paragraph(concepto, fontConcepto));
        cellConcepto.setBorder(Rectangle.NO_BORDER);
        cellConcepto.setPadding(5);
        table.addCell(cellConcepto);

        PdfPCell cellValor = new PdfPCell(new Paragraph("$" + String.format("%,.2f", valor), fontValor));
        cellValor.setBorder(Rectangle.NO_BORDER);
        cellValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellValor.setPadding(5);
        table.addCell(cellValor);
    }

    private void agregarFooter(Document document, Font font) throws DocumentException {
        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);
        footerTable.setSpacingBefore(30);

        PdfPCell footerCell = new PdfPCell();
        footerCell.setBorder(Rectangle.TOP);
        footerCell.setBorderColor(BaseColor.LIGHT_GRAY);
        footerCell.setBorderWidth(1);
        footerCell.setPaddingTop(10);
        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        footerCell.addElement(new Paragraph("¡Gracias por su compra!",
                new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC)));
        footerCell.addElement(new Paragraph("Factura generada el: " +
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), font));

        footerTable.addCell(footerCell);
        document.add(footerTable);
    }

    private BaseColor obtenerColorEstado(String estado) {
        if (estado == null) return null;

        switch (estado.toUpperCase()) {
            case "PAGADA":
                return new BaseColor(76, 175, 80);
            case "PENDIENTE":
                return new BaseColor(255, 193, 7);
            case "ANULADA":
                return new BaseColor(211, 47, 47);
            case "ABONADO":
                return new BaseColor(33, 150, 243);
            default:
                return null;
        }
    }
    private String formatDate(Object dateObj) {
        if (dateObj == null) {
            return "N/A";
        }

        try {
            if (dateObj instanceof java.sql.Date) {
                // Convertir java.sql.Date a java.util.Date
                java.sql.Date sqlDate = (java.sql.Date) dateObj;
                return new SimpleDateFormat("dd/MM/yyyy").format(new Date(sqlDate.getTime()));
            } else if (dateObj instanceof java.util.Date) {
                return new SimpleDateFormat("dd/MM/yyyy").format((Date) dateObj);
            } else if (dateObj instanceof java.time.LocalDate) {
                return ((java.time.LocalDate) dateObj).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else if (dateObj instanceof java.time.LocalDateTime) {
                return ((java.time.LocalDateTime) dateObj).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else {
                return dateObj.toString();
            }
        } catch (Exception e) {
            return "Fecha inválida";
        }
    }
    public byte[] generarReporteFacturas(List<Factura> facturas, Usuario usuario)
            throws DocumentException, IOException {

        System.out.println("=== GENERANDO REPORTE DE " + facturas.size() + " FACTURAS ===");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate()); // Horizontal para más espacio
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();

        // Usar FontFactory para evitar warnings de fuentes
        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font subtituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font normalBoldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

        // Encabezado con logo (opcional)
        try {
            Paragraph header = new Paragraph("AGROSELLNOVA", tituloFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(5);
            document.add(header);

            Paragraph subheader = new Paragraph("Reporte de Facturas del Cliente", subtituloFont);
            subheader.setAlignment(Element.ALIGN_CENTER);
            subheader.setSpacingAfter(15);
            document.add(subheader);
        } catch (Exception e) {
            // Continuar sin encabezado si hay error
        }

        // Información del cliente
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(10);
        infoTable.setSpacingAfter(15);

        PdfPCell infoCell1 = new PdfPCell();
        infoCell1.setBorder(Rectangle.NO_BORDER);
        infoCell1.addElement(new Paragraph("Cliente: " +
                (usuario.getNombre() != null ? usuario.getNombre() : "N/A"), normalFont));
        infoCell1.addElement(new Paragraph("Documento: " +
                (usuario.getDocumento() != null ? usuario.getDocumento() : "N/A"), normalFont));
        infoCell1.addElement(new Paragraph("Correo: " +
                (usuario.getCorreo() != null ? usuario.getCorreo() : "N/A"), normalFont));
        infoTable.addCell(infoCell1);

        PdfPCell infoCell2 = new PdfPCell();
        infoCell2.setBorder(Rectangle.NO_BORDER);
        infoCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        infoCell2.addElement(new Paragraph("Fecha de generación: " +
                new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), normalFont));
        infoCell2.addElement(new Paragraph("Total de facturas: " + facturas.size(), normalFont));
        infoTable.addCell(infoCell2);

        document.add(infoTable);

        // Línea separadora
        PdfPTable lineTable = new PdfPTable(1);
        lineTable.setWidthPercentage(100);
        lineTable.setSpacingBefore(5);
        lineTable.setSpacingAfter(10);

        PdfPCell lineCell = new PdfPCell();
        lineCell.setBorder(Rectangle.BOTTOM);
        lineCell.setBorderColor(BaseColor.GRAY);
        lineCell.setBorderWidth(1);
        lineCell.setFixedHeight(1);
        lineCell.setPadding(0);

        lineTable.addCell(lineCell);
        document.add(lineTable);

        // Tabla de facturas
        if (facturas.isEmpty()) {
            Paragraph noData = new Paragraph("No hay facturas para mostrar", tituloFont);
            noData.setAlignment(Element.ALIGN_CENTER);
            noData.setSpacingBefore(50);
            document.add(noData);
        } else {
            // Tabla con 7 columnas
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);

            // Configurar anchos de columnas (en porcentajes)
            float[] columnWidths = {10f, 15f, 12f, 10f, 15f, 15f, 8f};
            table.setWidths(columnWidths);

            // Encabezados
            String[] headers = {"#", "N° Factura", "Fecha", "Total", "Estado", "Método Pago", "Productos"};

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, normalBoldFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new BaseColor(232, 245, 233)); // Verde claro
                cell.setPadding(8);
                cell.setBorderWidth(1);
                cell.setBorderColor(BaseColor.DARK_GRAY);
                table.addCell(cell);
            }

            // Filas de datos
            int contador = 1;
            double totalGeneral = 0.0;

            for (Factura factura : facturas) {
                try {
                    // Columna 1: Contador
                    PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(contador++), normalFont));
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell1.setPadding(5);
                    table.addCell(cell1);

                    // Columna 2: Número de factura
                    PdfPCell cell2 = new PdfPCell(new Paragraph(
                            factura.getNumeroFactura() != null ? factura.getNumeroFactura() : "N/A",
                            normalFont));
                    cell2.setPadding(5);
                    table.addCell(cell2);

                    // Columna 3: Fecha (USAR EL MÉTODO SEGURO)
                    String fechaFormateada = formatDate(factura.getFechaEmision());
                    PdfPCell cell3 = new PdfPCell(new Paragraph(fechaFormateada, normalFont));
                    cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell3.setPadding(5);
                    table.addCell(cell3);

                    // Columna 4: Total
                    double totalFactura = factura.getTotal() != null ? factura.getTotal() : 0.0;
                    totalGeneral += totalFactura;
                    PdfPCell cell4 = new PdfPCell(new Paragraph(
                            "$" + String.format("%,.2f", totalFactura), normalFont));
                    cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell4.setPadding(5);
                    table.addCell(cell4);

                    // Columna 5: Estado con color
                    String estado = factura.getEstadoFactura() != null ? factura.getEstadoFactura() : "PENDIENTE";
                    PdfPCell cell5 = new PdfPCell(new Paragraph(estado, normalFont));
                    cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell5.setPadding(5);

                    // Aplicar color según estado
                    BaseColor colorEstado = obtenerColorEstado(estado);
                    if (colorEstado != null) {
                        cell5.setBackgroundColor(colorEstado);
                    }
                    table.addCell(cell5);

                    // Columna 6: Método de pago
                    String metodoPago = "N/A";
                    if (factura.getPago() != null && factura.getPago().getMetodoPago() != null) {
                        metodoPago = factura.getPago().getMetodoPago();
                    }
                    PdfPCell cell6 = new PdfPCell(new Paragraph(metodoPago, normalFont));
                    cell6.setPadding(5);
                    table.addCell(cell6);

                    // Columna 7: Número de productos
                    int productosCount = 0;
                    if (factura.getDetalles() != null) {
                        productosCount = factura.getDetalles().size();
                    }
                    PdfPCell cell7 = new PdfPCell(new Paragraph(String.valueOf(productosCount), normalFont));
                    cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell7.setPadding(5);
                    table.addCell(cell7);

                } catch (Exception e) {
                    System.out.println("Error procesando factura ID " + factura.getIdFactura() + ": " + e.getMessage());
                    // Continuar con la siguiente factura
                    continue;
                }
            }

            document.add(table);

            // Resumen estadístico
            PdfPTable resumenTable = new PdfPTable(4);
            resumenTable.setWidthPercentage(80);
            resumenTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            resumenTable.setSpacingBefore(20);
            resumenTable.setSpacingAfter(10);

            // Totales por estado
            Map<String, Integer> conteoEstados = new HashMap<>();
            Map<String, Double> totalPorEstado = new HashMap<>();

            for (Factura factura : facturas) {
                String estado = factura.getEstadoFactura() != null ? factura.getEstadoFactura() : "SIN ESTADO";
                double total = factura.getTotal() != null ? factura.getTotal() : 0.0;

                conteoEstados.put(estado, conteoEstados.getOrDefault(estado, 0) + 1);
                totalPorEstado.put(estado, totalPorEstado.getOrDefault(estado, 0.0) + total);
            }

            // Encabezado del resumen
            PdfPCell resumenHeader = new PdfPCell(new Paragraph("RESUMEN ESTADÍSTICO", normalBoldFont));
            resumenHeader.setColspan(4);
            resumenHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            resumenHeader.setBackgroundColor(new BaseColor(240, 240, 240));
            resumenHeader.setPadding(8);
            resumenTable.addCell(resumenHeader);

            // Subtítulos
            resumenTable.addCell(new PdfPCell(new Paragraph("Estado", normalBoldFont)));
            resumenTable.addCell(new PdfPCell(new Paragraph("Cantidad", normalBoldFont)));
            resumenTable.addCell(new PdfPCell(new Paragraph("Total", normalBoldFont)));
            resumenTable.addCell(new PdfPCell(new Paragraph("%", normalBoldFont)));

            // Filas del resumen
            for (Map.Entry<String, Integer> entry : conteoEstados.entrySet()) {
                String estado = entry.getKey();
                int cantidad = entry.getValue();
                double totalEstado = totalPorEstado.getOrDefault(estado, 0.0);
                double porcentaje = facturas.size() > 0 ? (cantidad * 100.0 / facturas.size()) : 0.0;

                resumenTable.addCell(new PdfPCell(new Paragraph(estado, normalFont)));
                resumenTable.addCell(new PdfPCell(new Paragraph(String.valueOf(cantidad), normalFont)));
                resumenTable.addCell(new PdfPCell(new Paragraph("$" + String.format("%,.2f", totalEstado), normalFont)));
                resumenTable.addCell(new PdfPCell(new Paragraph(String.format("%.1f%%", porcentaje), normalFont)));
            }

            document.add(resumenTable);

            // Total general
            Paragraph totalGeneralParrafo = new Paragraph(
                    "TOTAL GENERAL: $" + String.format("%,.2f", totalGeneral),
                    new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(46, 125, 50)));
            totalGeneralParrafo.setAlignment(Element.ALIGN_RIGHT);
            totalGeneralParrafo.setSpacingBefore(15);
            document.add(totalGeneralParrafo);
        }

        // Footer
        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);
        footerTable.setSpacingBefore(30);

        PdfPCell footerCell = new PdfPCell();
        footerCell.setBorder(Rectangle.TOP);
        footerCell.setBorderColor(BaseColor.LIGHT_GRAY);
        footerCell.setBorderWidth(1);
        footerCell.setPaddingTop(10);
        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        footerCell.addElement(new Paragraph("Reporte generado por Agrosellnova", footerFont));
        footerCell.addElement(new Paragraph("Página 1 de 1", footerFont));

        footerTable.addCell(footerCell);
        document.add(footerTable);

        document.close();
        writer.close();

        System.out.println("=== REPORTE GENERADO CON ÉXITO ===");
        return baos.toByteArray();
    }

}
