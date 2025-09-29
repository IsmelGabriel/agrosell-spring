package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.dto.DashboardDTO;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.modelo.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public DashboardDTO obtenerMetricasAdmin() {
        DashboardDTO dto = new DashboardDTO();

        List<Venta> ventas = ventaService.obtenerTodasLasVentas();
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();

        dto.setTotalVentas((long) (ventas != null ? ventas.size() : 0));
        dto.setTotalProductos((long) (productos != null ? productos.size() : 0));
        dto.setTotalUsuarios((long) (usuarios != null ? usuarios.size() : 0));

        dto.setVentasPorFecha(ventas != null ? ventas.stream()
                .collect(Collectors.groupingBy(v -> v.getFechaVenta().toString(), Collectors.counting()))
                : Map.of());

        dto.setProductosPorEstado(productos != null ? productos.stream()
                .collect(Collectors.groupingBy(Producto::getEstado, Collectors.counting()))
                : Map.of());

        dto.setUsuariosPorRol(usuarios != null ? usuarios.stream()
                .collect(Collectors.groupingBy(Usuario::getRol, Collectors.counting()))
                : Map.of());

        dto.setUsuariosPorEstado(usuarios != null ? usuarios.stream()
                .collect(Collectors.groupingBy(Usuario::getEstado, Collectors.counting()))
                : Map.of());

        return dto;
    }


    @Override
    public DashboardDTO obtenerMetricasCampesino(Long idCampesino) {
        DashboardDTO dto = new DashboardDTO();

        Usuario campesino = usuarioService.obtenerTodosLosUsuarios()
                .stream()
                .filter(u -> u.getId().equals(idCampesino))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Campesino no encontrado"));

        // Productos del campesino
        List<Producto> productos = productoService.obtenerProductosPorUsuario(campesino.getNombreUsuario());

        // Ventas de los productos del campesino
        List<Venta> ventas = ventaService.obtenerTodasLasVentas()
                .stream()
                .filter(v -> v.getProducto().getUsuarioCampesino().equals(campesino.getNombreUsuario()))
                .toList();

        dto.setTotalProductos((long) productos.size());
        dto.setTotalVentas((long) ventas.size());

        // Productos agrupados por estado
        dto.setProductosPorEstado(
                productos.stream()
                        .collect(Collectors.groupingBy(
                                Producto::getEstado,
                                Collectors.counting()
                        ))
        );

        // Ventas agrupadas por fecha
        dto.setVentasPorFecha(
                ventas.stream()
                        .collect(Collectors.groupingBy(
                                v -> v.getFechaVenta().toString(),
                                Collectors.counting()
                        ))
        );

        return dto;
    }

    @Override
    public DashboardDTO obtenerMetricasCliente(Long idCliente) {
        DashboardDTO dto = new DashboardDTO();

        Usuario cliente = usuarioService.obtenerTodosLosUsuarios()
                .stream()
                .filter(u -> u.getId().equals(idCliente))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Ventas hechas por el cliente
        List<Venta> ventas = ventaService.obtenerTodasLasVentas()
                .stream()
                .filter(v -> v.getComprador().getId().equals(cliente.getId()))
                .toList();

        dto.setTotalVentas((long) ventas.size());

        // Ventas agrupadas por fecha
        dto.setVentasPorFecha(
                ventas.stream()
                        .collect(Collectors.groupingBy(
                                v -> v.getFechaVenta().toString(),
                                Collectors.counting()
                        ))
        );

        return dto;
    }

}
