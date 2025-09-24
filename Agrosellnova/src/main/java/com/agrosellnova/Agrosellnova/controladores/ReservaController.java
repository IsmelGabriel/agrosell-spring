package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Reserva;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import com.agrosellnova.Agrosellnova.servicio.ReservaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.agrosellnova.Agrosellnova.repositorio.ReservaRepository;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
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
        return "/forms/formulario_reserva";
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
            // 📌 Obtener usuario de la sesión
            String nombreUsuario = (String) session.getAttribute("usuario");
            if (nombreUsuario == null) {
                return "redirect:/public/index";
            }

            // 📂 Guardar la imagen
            String nombreArchivo = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
            String rutaAbsoluta = new File("Agrosellnova/src/main/resources/static/img").getAbsolutePath();
            Files.createDirectories(Paths.get(rutaAbsoluta));
            Path path = Paths.get(rutaAbsoluta, nombreArchivo);
            Files.write(path, imagen.getBytes());
            String rutaRelativa = "../img/" + nombreArchivo;

            // 📝 Crear el producto
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

}


