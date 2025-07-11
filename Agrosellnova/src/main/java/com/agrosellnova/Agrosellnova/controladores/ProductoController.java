package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/public/productos")
    public String mostrarProductosPublicos(
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
            productos = productoService.obtenerTodosLosProductos();
        }

        model.addAttribute("productos", productos);
        return "public/productos";
    }


    @GetMapping("/private/gestionar_productos")
    public String mostrarProductos(
            @RequestParam(required = false) String criterio,
            @RequestParam(required = false) String valor,
            HttpSession session,
            Model model) {

        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null || !rol.equals("productor")) {
            return "redirect:/public/index";
        }

        List<Producto> productos;
        if (criterio != null && valor != null && !valor.isBlank()) {
            productos = productoService.filtrarProductos(usuario, criterio, valor);
        } else {
            productos = productoService.obtenerProductosPorProductor(usuario);
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("listaProductos", productos);

        return "private/gestionar_productos";
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
            return "error";
        }
    }
}
