package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
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
import java.util.List;
import java.util.UUID;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioServiceImpl usuarioService;

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
    public String gestionarProductos(HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        List<Producto> listaProductos = productoService.obtenerProductosPorProductor(usuario);

        model.addAttribute("listaProductos", listaProductos);
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);

        return "private/gestionar_productos";
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
                String rutaAbsoluta = new File("src/main/resources/static/img").getAbsolutePath();
                Path path = Paths.get(rutaAbsoluta + File.separator + nombreArchivo);
                Files.write(path, nuevaImagen.getBytes());

                existente.setImagen("../img/" + nombreArchivo);
            }

            productoRepository.save(existente);
            return "redirect:/private/gestionar_productos";

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

}