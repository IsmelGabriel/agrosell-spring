package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
public class PaginaController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    private final List<String> paginasRestringidas = List.of("cerrar_sesion", "api");

    @GetMapping("/public/{pagina}")
    public String mostrarPaginaPublica(@PathVariable("pagina") String pagina, HttpSession session, Model model) {
        if ("productos".equals(pagina)) {
            return "redirect:/public/productos";
        }

        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        if (paginasRestringidas.contains(pagina)) {
            return "redirect:/error";
        }

        return "public/" + pagina;
    }

    @GetMapping("/forms/{pagina}")
    public String mostrarPaginaForms(@PathVariable("pagina") String pagina, HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        if (paginasRestringidas.contains(pagina)) {
            return "redirect:/error";
        }

        return "forms/" + pagina;
    }

    @GetMapping("/private/{pagina}")
    public String mostrarPaginaPrivada(@PathVariable("pagina") String pagina, HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario user = usuarioService.buscarPorNombreUsuario(nombreUsuario);

        model.addAttribute("usuario", nombreUsuario);
        model.addAttribute("rol", rol);
        model.addAttribute("user", user);

        return "private/" + pagina;
    }

    @GetMapping("/forms/formulario_pago")
    public String mostrarFormularioPago(HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");
        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }

        Usuario comprador = usuarioService.buscarPorNombreUsuario(nombreUsuario);
        if (comprador == null) {
            return "redirect:/public/index";
        }

        model.addAttribute("nombre", comprador.getNombre());
        model.addAttribute("correo", comprador.getCorreo());
        model.addAttribute("direccion", comprador.getDireccion());

        return "forms/formulario_pago";
    }

    @PostMapping("/guardar_producto")
    public String guardarProducto(
            @RequestParam("usuario") String nombreUsuario,
            @RequestParam("productoImagen") MultipartFile imagen,
            @RequestParam("nombreProducto") String nombre,
            @RequestParam("precio") double precio,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("pesoKg") double pesoKg,
            @RequestParam("stock") int stock
    ) {
        try {

            String nombreArchivo = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
            String rutaRelativa = "../img/" + nombreArchivo;
            String rutaAbsoluta = new File("src/main/resources/static/img").getAbsolutePath();

            byte[] bytes = imagen.getBytes();
            Path path = Paths.get(rutaAbsoluta + File.separator + nombreArchivo);
            Files.write(path, bytes);

            Producto producto = new Producto();
            producto.setUsuarioCampesino(nombreUsuario);
            producto.setImagen(rutaRelativa);
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setDescripcion(descripcion);
            producto.setPesoKg(pesoKg);
            producto.setStock(stock);
            producto.setFechaCosecha(LocalDate.now());

            productoRepository.save(producto);
            return "redirect:/private/gestionar_productos";

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @GetMapping("/forms/editar_producto")
    public String mostrarFormularioEdicion(@RequestParam("id") Long id, HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");
        if (nombreUsuario == null) {
            return "redirect:/public/index";
        }
        Producto producto = productoService.obtenerPorId(id);
        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);

        model.addAttribute("usuario", usuario.getNombreUsuario());
        model.addAttribute("rol", usuario.getRol());
        model.addAttribute("producto", producto);

        return "forms/editar_producto";
    }

}
