package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Calificaciones;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.servicio.CalificacionesService;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/private")
public class CalificacionesController {

    private final CalificacionesService calificacionesService;
    private final ProductoService productoService;

    @Autowired
    public CalificacionesController(CalificacionesService calificacionesService, ProductoService productoService) {
        this.calificacionesService = calificacionesService;
        this.productoService = productoService;
    }

    @PostMapping("/guardar_calificacion")
    public String guardarCalificacion(@RequestParam("idVenta") Long idVenta,
                                      @RequestParam("productoId") Long productoId,
                                      @RequestParam("estrellas") Integer estrellas,
                                      @RequestParam("comentario") String comentario,
                                      HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        Producto producto = productoService.obtenerPorId(productoId);

        Calificaciones calificacion = new Calificaciones();
        calificacion.setProducto(producto);
        calificacion.setUsuario(usuario);
        calificacion.setCalificacion(estrellas);
        calificacion.setComentario(comentario);
        calificacion.setFechaCreacion(LocalDateTime.now());

        calificacionesService.guardar(calificacion);

        return "redirect:/private/gestionar_compra?calificado=true";
    }
}