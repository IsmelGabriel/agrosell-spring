package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.repositorio.UsuarioRepository;
import com.agrosellnova.Agrosellnova.servicio.EmailService;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/public/productos")
    public String mostrarProductos(
            @RequestParam(name = "producto", required = false) String nombre,
            @RequestParam(name = "orden", required = false, defaultValue = "") String orden,
            HttpSession session,
            Model model) {

        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        List<Producto> productos;
        if (nombre != null || !orden.isBlank()) {
            productos = productoService.buscarProductosFiltrados(nombre, orden);
        } else {
            productos = productoService.obtenerProductosDisponibles();
        }

        model.addAttribute("productos", productos);
        return "public/productos";
    }

    @GetMapping("/private/gestionar_productos")
    public String gestionarProductos(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            HttpSession session, Model model) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        List<Producto> listaProductos;
        if (criterio != null && valor != null && !criterio.isEmpty() && !valor.isEmpty()) {
            listaProductos = productoService.filtrarProductos(usuario, criterio, valor);
        }else {
            listaProductos = productoRepository.findByUsuarioCampesinoOrderByIdDesc(usuario);
        }

        model.addAttribute("listaProductos", listaProductos);
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);

        return "private/gestionar_productos";
    }

    @PostMapping("/guardar_producto")
    public String guardarProductoReserva(
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
            String rutaAbsoluta = new File("Agrosellnova/src/main/resources/static/img/productos").getAbsolutePath();

            Files.createDirectories(Paths.get(rutaAbsoluta));
            Path path = Paths.get(rutaAbsoluta, nombreArchivo);
            Files.write(path, imagen.getBytes());
            String rutaRelativa = "../img/productos/" + nombreArchivo;

            Producto producto = new Producto();
            producto.setUsuarioCampesino(nombreUsuario);
            producto.setImagen(rutaRelativa);
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setDescripcion(descripcion);
            producto.setPesoKg(pesoKg);
            producto.setStock(stock);
            producto.setEstado("Disponible");
            producto.setFechaCosecha(LocalDate.now());

            // Solo delega al servicio
            productoService.guardarProducto(producto);

            return "redirect:/private/gestionar_productos";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }


    @PostMapping("/producto/editar")
    public String editarProducto(
            @ModelAttribute Producto producto,
            @RequestParam(value = "productoImagen", required = false) MultipartFile nuevaImagen
    ) {
        try {
            Producto existente = productoService.obtenerPorId(producto.getId());
            if (existente == null) {
                return "redirect:/error";
            }

            existente.setNombre(producto.getNombre());
            existente.setPrecio(producto.getPrecio());
            existente.setDescripcion(producto.getDescripcion());
            existente.setPesoKg(producto.getPesoKg());
            existente.setStock(producto.getStock());

            if (nuevaImagen != null && !nuevaImagen.isEmpty()) {
                String nombreArchivo = UUID.randomUUID().toString() + "_" + nuevaImagen.getOriginalFilename();
                String rutaAbsoluta = new File("src/main/resources/static/img/productos").getAbsolutePath();
                Path path = Paths.get(rutaAbsoluta + File.separator + nombreArchivo);
                Files.write(path, nuevaImagen.getBytes());

                existente.setImagen("../img/productos/" + nombreArchivo);
            }

            productoRepository.save(existente);
            return "redirect:/private/gestionar_productos";

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @GetMapping("/private/actualizarEstadoProducto")
    public String actualizarEstadoProducto(@RequestParam("id") Long id, @RequestParam("estado") String estado) {
        Producto producto = productoService.obtenerPorId(id);
        if (producto != null) {
            producto.setEstado(estado);
            productoRepository.save(producto);
        }
        return "redirect:/private/gestionar_productos";
    }

}