package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.ConsultaSipsa;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Productor;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import com.agrosellnova.Agrosellnova.servicio.ProductorService;
import com.agrosellnova.Agrosellnova.servicio.SipsaService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PaginaController {
    @Autowired
    private ProductorService productorService;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private SipsaService sipsaService;

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

        return "redirect:forms/" + pagina;
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
        if ("productor".equals(rol) && user != null) {
            Optional<Productor> productorOpt = productorService.obtenerPorUsuario(user.getId().intValue());

            if (productorOpt.isPresent()) {
                model.addAttribute("productor", productorOpt.get());
            }
        }

        return "private/" + pagina;
    }

    @GetMapping("/forms/formulario_pago")
    public String mostrarFormularioPago(HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");

        if (usuario == null || rol == null) {
            return "redirect:/public/index";
        }

        Usuario comprador = usuarioService.buscarPorNombreUsuario(usuario);
        if (comprador == null) {
            return "redirect:/public/index";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("nombre", comprador.getNombre());
        model.addAttribute("correo", comprador.getCorreo());
        model.addAttribute("direccion", comprador.getDireccion());

        return "forms/formulario_pago";
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
            String rutaAbsoluta = new File("Agrosellnova/src/main/resources/static/img").getAbsolutePath();

            // Crear carpeta si no existe
            Files.createDirectories(Paths.get(rutaAbsoluta));
            Path path = Paths.get(rutaAbsoluta, nombreArchivo);
            Files.write(path, imagen.getBytes());
            String rutaRelativa = "../img/" + nombreArchivo;

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

            productoRepository.save(producto);

            return "redirect:/private/gestionar_productos";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }


    @GetMapping("/forms/editar_producto")
    public String mostrarFormularioEdicion(@RequestParam("id") Long id, HttpSession session, Model model) {
        String nombreUsuario = (String) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("rol");
        if (nombreUsuario == null || !Objects.equals(rol, "productor")) {
            return "redirect:/public/index";
        }
        Producto producto = productoService.obtenerPorId(id);
        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);

        model.addAttribute("usuario", usuario.getNombreUsuario());
        model.addAttribute("rol", usuario.getRol());
        model.addAttribute("producto", producto);

        return "forms/editar_producto";
    }

    @GetMapping("/public/inicio")
    public String mostrarInicio(
            @RequestParam(required = false, defaultValue = "ciudad") String tipoConsulta,
            Model model,
            HttpSession session) {

        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        // Productos destacados
        List<Producto> destacados = productoRepository.findTop4ByOrderByPrecioAsc();
        List<Producto> masStock = productoRepository.findTop4ByOrderByStockDesc();
        List<Producto> recientes = productoRepository.findTop4ByOrderByFechaCosechaDesc();

        model.addAttribute("productosDestacados", destacados);
        model.addAttribute("productosStock", masStock);
        model.addAttribute("productosRecientes", recientes);

        // Datos de SIPSA según el tipo de consulta seleccionado
        List<ConsultaSipsa> datosSipsa;

        switch (tipoConsulta) {
            case "mensual":
                datosSipsa = sipsaService.buscarPorTipoConsulta("mensual");
                model.addAttribute("tituloSeccion", "Datos Mensuales de Mayoristas");
                model.addAttribute("descripcionSeccion", "Información consolidada mensual de cantidades y precios de productos agrícolas en mercados mayoristas.");
                break;
            case "semanal":
                datosSipsa = sipsaService.buscarPorTipoConsulta("semanal");
                model.addAttribute("tituloSeccion", "Datos Semanales de Mayoristas");
                model.addAttribute("descripcionSeccion", "Información semanal actualizada de precios y cantidades en mercados mayoristas del país.");
                break;
            case "abastecimiento":
                datosSipsa = sipsaService.buscarPorTipoConsulta("abastecimiento");
                model.addAttribute("tituloSeccion", "Abastecimiento por Fuentes");
                model.addAttribute("descripcionSeccion", "Información sobre el abastecimiento de productos discriminado por fuentes y orígenes.");
                break;
            default: // ciudad
                datosSipsa = sipsaService.buscarPorTipoConsulta("ciudad");
                model.addAttribute("tituloSeccion", "Precios por Ciudad");
                model.addAttribute("descripcionSeccion", "Precios promedio de productos agrícolas en diferentes ciudades de Colombia.");
                break;
        }

        // Si no hay datos previos, obtener solo las últimas 10 consultas
        if (datosSipsa.isEmpty()) {
            datosSipsa = sipsaService.obtenerUltimasConsultas();
        }

        model.addAttribute("datosSipsa", datosSipsa);
        model.addAttribute("tipoConsultaActual", tipoConsulta);
        model.addAttribute("totalDatos", datosSipsa.size());

        return "public/inicio";
    }
    @PostMapping("/public/inicio/consultar-sipsa")
    public String consultarSipsaDesdeInicio(
            @RequestParam String tipoConsulta,
            RedirectAttributes redirectAttributes) {

        try {
            List<ConsultaSipsa> consultas;

            switch (tipoConsulta) {
                case "mensual":
                    consultas = sipsaService.consultarYGuardarDatosMensuales();
                    redirectAttributes.addFlashAttribute("mensaje",
                            "✓ Consulta mensual exitosa. Se obtuvieron " + consultas.size() + " registros.");
                    break;
                case "semanal":
                    consultas = sipsaService.consultarYGuardarDatosSemanales();
                    redirectAttributes.addFlashAttribute("mensaje",
                            "✓ Consulta semanal exitosa. Se obtuvieron " + consultas.size() + " registros.");
                    break;
                case "abastecimiento":
                    consultas = sipsaService.consultarYGuardarAbastecimiento();
                    redirectAttributes.addFlashAttribute("mensaje",
                            "✓ Consulta de abastecimiento exitosa. Se obtuvieron " + consultas.size() + " registros.");
                    break;
                default: // ciudad
                    consultas = sipsaService.consultarYGuardarPreciosCiudad();
                    redirectAttributes.addFlashAttribute("mensaje",
                            "✓ Consulta de precios por ciudad exitosa. Se obtuvieron " + consultas.size() + " registros.");
                    break;
            }

            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "✗ Error al consultar la API: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            e.printStackTrace();
        }

        return "redirect:/public/inicio?tipoConsulta=" + tipoConsulta;
    }
    @GetMapping("/public/inicio/buscar")
    public String buscarSipsaDesdeInicio(
            @RequestParam(required = false) String producto,
            @RequestParam(required = false, defaultValue = "ciudad") String tipoConsulta,
            Model model,
            HttpSession session) {

        String usuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);

        // Productos destacados
        List<Producto> destacados = productoRepository.findTop4ByOrderByPrecioAsc();
        model.addAttribute("productosDestacados", destacados);

        // Buscar en SIPSA
        List<ConsultaSipsa> datosSipsa;

        if (producto != null && !producto.isEmpty()) {
            datosSipsa = sipsaService.buscarPorProducto(producto);
            model.addAttribute("mensajeBusqueda", "Resultados para: " + producto);
        } else {
            datosSipsa = sipsaService.buscarPorTipoConsulta(tipoConsulta);
        }

        model.addAttribute("datosSipsa", datosSipsa);
        model.addAttribute("tipoConsultaActual", tipoConsulta);
        model.addAttribute("totalDatos", datosSipsa.size());
        model.addAttribute("productoBuscado", producto);

        return "public/inicio";
    }

}
