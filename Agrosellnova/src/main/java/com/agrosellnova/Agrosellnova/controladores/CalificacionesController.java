package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Calificaciones;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.repositorio.CalificacionesRepository;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.repositorio.UsuarioRepository;
import com.agrosellnova.Agrosellnova.servicio.CalificacionesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class CalificacionesController {

    @Autowired
    private CalificacionesService calificacionesService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/private/guardar_calificacion")
    public String guardarCalificacion(@RequestParam("idVenta") Long idVenta,
                                      @RequestParam("productoId") Long productoId,
                                      @RequestParam("estrellas") int estrellas,
                                      @RequestParam("comentario") String comentario,
                                      HttpSession session) {

        Long IdUsuario = (Long) session.getAttribute("idUsuario");

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));


        Usuario usuario = usuarioRepository.findById(IdUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Calificaciones calificacion = new Calificaciones();
        calificacion.setProducto(producto);
        calificacion.setUsuario(usuario);
        calificacion.setCalificacion(estrellas);
        calificacion.setComentario(comentario);
        calificacion.setFecha_creacion(LocalDate.now());

        calificacionesService.guardar(calificacion);

        return "redirect:/private/gestionar_compra";
    }
}
