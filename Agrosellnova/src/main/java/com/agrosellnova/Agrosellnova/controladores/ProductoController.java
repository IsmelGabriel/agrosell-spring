package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import com.agrosellnova.Agrosellnova.modelo.Producto;
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
    public String mostrarProductos(Model model) {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        model.addAttribute("productos", productos);
        return "public/productos";
    }
}
