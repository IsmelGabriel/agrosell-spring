package com.agrosellnova.Agrosellnova.controladores;


import com.agrosellnova.Agrosellnova.modelo.Factura;
import com.agrosellnova.Agrosellnova.modelo.DetalleFactura;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.servicio.FacturaPDFService;
import com.agrosellnova.Agrosellnova.servicio.FacturaService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import com.itextpdf.text.DocumentException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/private")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FacturaPDFService facturaPdfService;


    @GetMapping("/mis-facturas")
    public String listarMisFacturas(HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);
        List<Factura> facturas = facturaService.listarPorUsuario(usuario);

        model.addAttribute("facturas", facturas);
        model.addAttribute("rol", rol);
        model.addAttribute("usuario", usuario);

        return "private/mis_facturas";
    }


    @GetMapping("/detalle/{id}/json")
    @ResponseBody
    public Map<String, Object> obtenerDetalleFacturaJson(@PathVariable Long id, HttpSession session) {


        String nombreUsuario = (String) session.getAttribute("usuario");
        Map<String, Object> response = new HashMap<>();

        if (nombreUsuario == null) {
            System.out.println("ERROR: Usuario no autenticado");
            response.put("error", "No autorizado - Usuario no en sesión");
            return response;
        }



        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);
        if (usuario == null) {
            System.out.println("ERROR: Usuario no encontrado en BD");
            response.put("error", "Usuario no encontrado");
            return response;
        }

        Optional<Factura> facturaOpt = facturaService.buscarPorId(id);

        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();



            if (!factura.getUsuario().getId().equals(usuario.getId())) {
                System.out.println("ERROR: Factura no pertenece al usuario");
                response.put("error", "No autorizado - Factura no pertenece al usuario");
                return response;
            }

            List<DetalleFactura> detalles = facturaService.obtenerDetalles(id);


            response.put("numeroFactura", factura.getNumeroFactura());
            response.put("clienteNombre", factura.getUsuario().getNombre());
            response.put("clienteDocumento", factura.getUsuario().getDocumento());
            response.put("clienteCorreo", factura.getUsuario().getCorreo());
            response.put("clienteDireccion", factura.getPago().getDireccion());
            response.put("clienteTelefono", factura.getPago().getTelefono());
            response.put("fechaEmision", factura.getFechaEmision().toString());
            response.put("metodoPago", factura.getPago().getMetodoPago());
            response.put("estadoFactura", factura.getEstadoFactura());
            response.put("subtotal", factura.getSubtotal());
            response.put("descuento", factura.getDescuento());
            response.put("impuesto", factura.getImpuesto());
            response.put("total", factura.getTotal());


            List<Map<String, Object>> detallesSimple = new ArrayList<>();
            for (DetalleFactura detalle : detalles) {
                Map<String, Object> detalleMap = new HashMap<>();
                detalleMap.put("nombreProducto", detalle.getNombreProducto());
                detalleMap.put("cantidad", detalle.getCantidad());
                detalleMap.put("precioUnitario", detalle.getPrecioUnitario());
                detalleMap.put("subtotal", detalle.getSubtotal());
                detallesSimple.add(detalleMap);
            }
            response.put("detalles", detallesSimple);

            response.put("success", true);

            return response;
        }


        response.put("error", "Factura no encontrada");
        return response;
    }


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


            if (!factura.getUsuario().getId().equals(usuario.getId())) {
                return "redirect:/private/mis-facturas";
            }

            List<DetalleFactura> detalles = facturaService.obtenerDetalles(id);

            model.addAttribute("factura", factura);
            model.addAttribute("detalles", detalles);
            model.addAttribute("usuario", usuario);

            return "private/detalle_factura";
        }

        return "redirect:/private/mis-facturas";
    }


    @GetMapping("/buscar")
    public String buscarFactura(@RequestParam("numeroFactura") String numeroFactura,
                                HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);

        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            return "redirect:/private/mis-facturas";
        }

        Optional<Factura> facturaOpt = facturaService.buscarPorNumero(numeroFactura.trim());

        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();


            if (!factura.getUsuario().getId().equals(usuario.getId())) {
                model.addAttribute("error", "No tienes permiso para ver esta factura");
                List<Factura> facturas = facturaService.listarPorUsuario(usuario);
                model.addAttribute("facturas", facturas);
                model.addAttribute("usuario", usuario);
                return "private/mis_facturas";
            }

            return "redirect:/private/detalle/" + factura.getIdFactura();
        }

        model.addAttribute("error", "Factura no encontrada");
        List<Factura> facturas = facturaService.listarPorUsuario(usuario);
        model.addAttribute("facturas", facturas);
        model.addAttribute("usuario", usuario);
        return "private/mis_facturas";
    }


    @GetMapping("/filtrar")
    public String filtrarPorEstado(@RequestParam(value = "estado", required = false) String estado,
                                   @RequestParam(value = "numeroFactura", required = false) String numeroFactura,
                                   HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");
        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);
        List<Factura> facturas;

        if (numeroFactura != null && !numeroFactura.trim().isEmpty()) {
            Optional<Factura> facturaOpt = facturaService.buscarPorNumero(numeroFactura.trim());
            if (facturaOpt.isPresent() && facturaOpt.get().getUsuario().getId().equals(usuario.getId())) {
                facturas = List.of(facturaOpt.get());
            } else {
                facturas = List.of();
            }
        }

        else if (estado != null && !estado.trim().isEmpty()) {
            facturas = facturaService.listarPorUsuarioYEstado(usuario, estado);
            model.addAttribute("estadoFiltro", estado);
        }

        else {
            facturas = facturaService.listarPorUsuario(usuario);
        }

        model.addAttribute("facturas", facturas);
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        return "private/mis_facturas";
    }


    @GetMapping("/descargar/{id}/pdf")
    public ResponseEntity<byte[]> descargarFacturaPdf(@PathVariable Long id, HttpSession session) {


        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            System.out.println("ERROR: Usuario no autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Optional<Factura> facturaOpt = facturaService.buscarPorId(id);

            if (!facturaOpt.isPresent()) {
                System.out.println("ERROR: Factura no encontrada");
                return ResponseEntity.notFound().build();
            }

            Factura factura = facturaOpt.get();

            Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);

            if (usuario == null) {
                System.out.println("ERROR: Usuario no encontrado en BD");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }


            if (!factura.getUsuario().getId().equals(usuario.getId())) {
                System.out.println("ERROR: No autorizado - Factura no pertenece al usuario");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            byte[] pdfBytes = facturaPdfService.generarFacturaPdf(factura);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename("factura-" + factura.getNumeroFactura() + ".pdf")
                            .build()
            );
            headers.setContentLength(pdfBytes.length);


            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DocumentException | IOException e) {
            System.out.println("ERROR CRÍTICO al generar PDF: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar PDF: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            System.out.println("ERROR INESPERADO: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error inesperado: " + e.getMessage()).getBytes());
        }
    }
    @GetMapping("/exportar/facturas/pdf")
    public ResponseEntity<byte[]> exportarTodasFacturasPdf(HttpSession session) {
        String nombreUsuario = (String) session.getAttribute("usuario");

        if (nombreUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }


            List<Factura> facturas = facturaService.listarPorUsuario(usuario);

            for (int i = 0; i < Math.min(facturas.size(), 3); i++) {
                Factura f = facturas.get(i);
                System.out.println("Factura " + (i+1) + ": " + f.getNumeroFactura() +
                        ", Fecha: " + f.getFechaEmision() +
                        ", Tipo: " + (f.getFechaEmision() != null ? f.getFechaEmision().getClass().getName() : "NULL"));
            }

            byte[] pdfBytes = facturaPdfService.generarReporteFacturas(facturas, usuario);


            String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String filename = "facturas-" + usuario.getNombre().replaceAll("\\s+", "-") + "-" + fechaActual + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename(filename)
                            .build()
            );
            headers.setContentLength(pdfBytes.length);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DocumentException | IOException e) {
            System.out.println("ERROR al generar reporte PDF: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar reporte: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            System.out.println("ERROR inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error inesperado: " + e.getMessage()).getBytes());
        }
    }
}
