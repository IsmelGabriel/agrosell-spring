package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/public/productos")
    public String mostrarProductos(Model model, HttpSession session) {
        // Obtener usuario en sesión (si está autenticado)
        String nombreUsuario = (String) session.getAttribute("usuario");
        model.addAttribute("usuario", nombreUsuario);

        // Obtener lista de productos desde la base de datos
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        model.addAttribute("productos", productos);

        return "public/productos"; // src/main/resources/templates/public/productos.html
    }
}
