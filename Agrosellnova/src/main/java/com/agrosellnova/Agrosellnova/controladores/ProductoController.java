package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Producto;
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

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

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
            productos = productoService.obtenerTodosLosProductos();
        }

        model.addAttribute("productos", productos);
        return "public/productos";
    }
    @GetMapping("/private/gestionar_productos")
    public String gestionarProductos(HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null || !rol.equals("productor")) {
            return "redirect:/public/index";
        }

        List<Producto> productos = productoService.listarProductosPorUsuario(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        model.addAttribute("listaProductos", productos);

        return "private/gestionar_productos";
    }
    @PostMapping("/guardar_producto")
    public String guardarProducto(@RequestParam("productoImagen") MultipartFile archivo,
                                  @RequestParam("nombreProducto") String nombre,
                                  @RequestParam("precio") Double precio,
                                  @RequestParam("descripcion") String descripcion,
                                  @RequestParam("pesoKg") Double pesoKg,
                                  @RequestParam("stock") Integer stock,
                                  @RequestParam("usuario") String usuario,
                                  HttpSession session) {

        try {
            // Guardar la imagen en la carpeta static/imagenes/
            String nombreArchivo = archivo.getOriginalFilename();
            String ruta = new File("src/main/resources/static/imagenes").getAbsolutePath();
            Path rutaCompleta = Paths.get(ruta + File.separator + nombreArchivo);
            Files.write(rutaCompleta, archivo.getBytes());

            // Crear el producto
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setDescripcion(descripcion);
            producto.setPesoKg(pesoKg);
            producto.setStock(stock);
            producto.setUsuarioCampesino(usuario);
            producto.setImagen(nombreArchivo);
            producto.setFechaCosecha(LocalDate.now()); // Puedes modificar si deseas

            productoService.guardarProducto(producto);

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/private/ofertar_producto?error";
        }

        return "redirect:/private/gestionar_productos?success";
    }

}

