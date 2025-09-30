package com.agrosellnova.Agrosellnova.controladores;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.repositorio.VentaRepository;
import com.agrosellnova.Agrosellnova.servicio.ProductoService;
import com.agrosellnova.Agrosellnova.servicio.UsuarioService;
import com.agrosellnova.Agrosellnova.servicio.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping
public class DashboardController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @RequestMapping("/public/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");

        // Top 4 ventas recientes
        List<Venta> ventasRecientes = ventaRepository.findTop4ByOrderByIdVentaDesc();

        // Estadísticas
        Double totalVentas = ventaRepository.totalVentas();
        Double totalProductos = ventaRepository.totalProductosVendidos();
        Long totalClientes = ventaRepository.totalClientes();

        // Añadir al modelo
        model.addAttribute("ventasRecientes", ventasRecientes);
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalVentas", totalVentas);
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("totalClientes", totalClientes);

        System.out.println("Total Ventas: " + totalVentas);
        System.out.println("Total Productos Vendidos: " + totalProductos);
        System.out.println("Total Clientes: " + totalClientes);

        return "public/dashboard";
    }
}
