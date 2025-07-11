package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.modelo.Pago;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.repositorio.PagoRepository;
import com.agrosellnova.Agrosellnova.servicio.UsuarioServiceImpl;
import com.agrosellnova.Agrosellnova.servicio.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/public")
public class PagoController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @PostMapping("/registrarPago")
    public String procesarPago(
            @RequestParam("metodoPago") String metodoPago,
            @RequestParam("tarjeta") String tarjeta,
            @RequestParam("fechaExpiracion") String fechaExpiracion,
            @RequestParam("cvv") String cvv,
            @RequestParam("direccion") String direccion,
            @RequestParam("telefono") String telefono,
            @RequestParam("carrito") String carritoJson,
            HttpSession session,
            Model model
    ) {
        String nombreUsuario = (String) session.getAttribute("usuario");
        Usuario comprador = usuarioService.buscarPorNombreUsuario(nombreUsuario);

        if (comprador == null) {
            return "redirect:/public/index";
        }

        try {
            List<Map<String, Object>> carrito = new com.fasterxml.jackson.databind.ObjectMapper().readValue(carritoJson, List.class);

            for (Map<String, Object> item : carrito) {
                Long idProducto = Long.valueOf(item.get("id").toString());
                Double cantidad = Double.valueOf(item.get("cantidad").toString());

                Producto producto = productoRepository.findById(idProducto).orElse(null);

                if (producto != null) {
                    Venta venta = new Venta();
                    venta.setProducto(producto);
                    venta.setCantidadKg(cantidad);
                    venta.setTotalVenta(producto.getPrecio() * cantidad);
                    venta.setComprador(comprador);

                    Usuario vendedor = usuarioService.buscarPorNombreUsuario(producto.getUsuarioCampesino());
                    venta.setVendedor(vendedor);

                    venta.setFechaVenta(LocalDate.now());

                    ventaService.guardarVenta(venta);
                }
            }

            // Registrar pago en la tabla "pago"
            Pago pago = new Pago();
            pago.setNombre(comprador.getNombre());
            pago.setCorreo(comprador.getCorreo());
            pago.setTelefono(telefono);
            pago.setMetodoPago(metodoPago);
            pago.setDireccion(direccion);
            pago.setFechaEmision(LocalDate.now());

            pagoRepository.save(pago);

            model.addAttribute("mensaje", "¡Pago exitoso! Gracias por tu compra.");
            return "public/pago_exitoso";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al procesar el pago.");
            return "redirect:/error";
        }
    }
}
