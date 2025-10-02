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
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping
public class DashboardController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VentaService ventaService;

    @RequestMapping("/private/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String rol= (String)  session.getAttribute("rol");
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
        model.addAttribute("rol", rol);



        System.out.println("Total Ventas: " + totalVentas);
        System.out.println("Total Productos Vendidos: " + totalProductos);
        System.out.println("Total Clientes: " + totalClientes);

        return "private/dashboard";
    }

    @GetMapping("/dashboard/ventas-mensuales")
    @ResponseBody
    public List<Map<String, Object>> obtenerVentasMensuales() {
        List<Object[]> resultados = ventaService.obtenerVentasMensuales();
        List<Map<String, Object>> datos = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("mes", fila[0]);     // número de mes (1–12)
            map.put("total", fila[1]);   // total vendido
            datos.add(map);
        }
        return datos;
    }

    @GetMapping("/dashboard/productos-mas-vendidos")
    @ResponseBody
    public List<Map<String, Object>> obtenerProductosMasVendidos() {
        List<Object[]> resultados = ventaService.obtenerProductosMasVendidos();
        List<Map<String, Object>> datos = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("producto", fila[0]);
            map.put("cantidad", fila[1]);
            datos.add(map);
        }
        return datos;
    }


}
