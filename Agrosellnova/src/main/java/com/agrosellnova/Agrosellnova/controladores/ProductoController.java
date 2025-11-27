package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Calificaciones;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Productor;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.repositorio.ProductorRepository;
import com.agrosellnova.Agrosellnova.repositorio.UsuarioRepository;
import com.agrosellnova.Agrosellnova.servicio.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ProductoController {

    @Autowired
    private CalificacionesService calificacionesService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ImgBBService imgBBService;

    @Autowired
    private ProductorRepository productorRepository;

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

        Map<Long, Usuario> productores= new HashMap<>();
        Map<Long, Productor>productoresInfo= new HashMap<>();
        Map<Long, Double >promedios=new HashMap<>();
        Map<Long, List<Calificaciones>>resenas=new HashMap<>();
        Map<Long, Long >totalCalificaciones=new HashMap<>();
        Map<Long, String> ultimaResena = new HashMap<>();

        for (Producto p : productos)
        {
            Usuario prod=usuarioRepository.findByNombreUsuario(p.getUsuarioCampesino());
            Productor productorInfo = productorRepository.findAllByIdUsuario(prod.getId().intValue()).stream().findFirst().orElse(null);
            if (prod != null)
            {
                productores.put(p.getId(),prod);
            }

            if (productorInfo != null)
            {
                productoresInfo.put(p.getId(),productorInfo);
            }

            promedios.put(p.getId(),calificacionesService.ObtenerPromedioByProducto(p.getId()));
            resenas.put(p.getId(),calificacionesService.listarPorProductoId(p.getId()));
            totalCalificaciones.put(p.getId(),calificacionesService.contarCalificacionesPorProducto(p.getId()));


            List<Calificaciones> lista = calificacionesService.listarPorProductoId(p.getId());
            resenas.put(p.getId(), lista);

            if (!lista.isEmpty()) {
                ultimaResena.put(p.getId(), lista.get(lista.size() - 1).getComentario());
            } else {
                ultimaResena.put(p.getId(), "Sin reseñas");
            }
        }

        model.addAttribute("productos", productos);
        model.addAttribute("productores",productores);
        model.addAttribute("promedios",promedios);
        model.addAttribute("resenas",resenas);
        model.addAttribute("totalCalificaciones",totalCalificaciones);
        model.addAttribute("ultimaResena", ultimaResena);
        model.addAttribute("productoresInfo", productoresInfo);


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
            String imagenUrl = imgBBService.uploadImage(imagen);

            Producto producto = new Producto();
            producto.setUsuarioCampesino(nombreUsuario);
            producto.setImagen(imagenUrl);
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
                String nuevaImagenUrl = imgBBService.uploadImage(nuevaImagen);
                existente.setImagen(nuevaImagenUrl);
            }

            productoRepository.save(existente);
            return "redirect:/private/gestionar_productos";

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        } catch (Exception e) {
            throw new RuntimeException(e);
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