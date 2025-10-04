package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificacionAsyncService notificacionAsyncService;

    @Override
    public void guardarProducto(Producto producto) {
        productoRepository.save(producto);

        List<Usuario> clientes = usuarioRepository.findCorreoByRol("cliente");
        List<String> correos = clientes.stream()
                .map(Usuario::getCorreo)
                .collect(Collectors.toList());

        if (!correos.isEmpty()) {
            notificacionAsyncService.enviarCorreosAsync(correos, producto.getNombre(), producto.getPrecio());
        }
    }


    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAllByOrderByIdDesc();
    }

    @Override
    public List<Producto> buscarProductosFiltrados(String nombre, String orden) {
        if (nombre == null) nombre = "";

        switch (orden) {
            case "precio_menor":
                return productoRepository.findByNombreContainingIgnoreCaseOrderByPrecioAsc(nombre);
            case "precio_mayor":
                return productoRepository.findByNombreContainingIgnoreCaseOrderByPrecioDesc(nombre);
            case "nombre":
                return productoRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(nombre);
            default:
                return productoRepository.findByNombreContainingIgnoreCaseOrderByIdDesc(nombre);
        }
    }

    @Override
    public List<Producto> obtenerProductosPorUsuario(String usuarioCampesino) {
        return productoRepository.findByUsuarioCampesino(usuarioCampesino);
    }

    @Override
    public List<Producto> obtenerProductosPorProductor(String usuario) {
        return productoRepository.findByUsuarioCampesino(usuario);
    }

    @Override
    public List<Producto> filtrarProductos(String usuario, String criterio, String valor) {
        switch (criterio.toLowerCase()) {
            case "id":
                try {
                    Long id = Long.parseLong(valor);
                    Producto producto = productoRepository.findByIdAndUsuarioCampesino(id, usuario);
                    return producto != null ? List.of(producto) : List.of();
                } catch (NumberFormatException e) {
                    return List.of();
                }

            case "producto":
                return productoRepository.findByNombreContainingIgnoreCaseAndUsuarioCampesino(valor, usuario);

            case "fecha":
                try {
                    LocalDate fecha = LocalDate.parse(valor);
                    return productoRepository.findByFechaCosechaAndUsuarioCampesino(fecha, usuario);
                } catch (Exception e) {
                    return List.of();
                }

            default:
                return List.of();
        }
    }

    @Override
    public List<Producto> obtenerProductosPorEstado(String estado) {
        return productoRepository.findByEstadoContainingIgnoreCase(estado);
    }

    @Override
    public List<Producto> obtenerProductosDisponibles() {
        return productoRepository.findProductosDisponibles();
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Override
    public List<Producto> obtenerProductosParaReserva() {
        return productoRepository.findByEstado("Proximo a salir");
    }


    @Override
    public void actualizarProducto(Producto producto) {
        productoRepository.save(producto);
    }


    @Override
    public Long obtenerCantidadTotalProductos() {
        return productoRepository.count();
    }

    @Override
    public Long obtenerCantidadProductosDisponibles() {
        return (long) productoRepository.findProductosDisponibles().size();
    }

    @Override
    public Long obtenerCantidadProductosPorEstado(String estado) {
        return (long) productoRepository.findByEstadoContainingIgnoreCase(estado).size();
    }

    @Override
    public List<Object[]> obtenerProductosPorEstado() {
        return productoRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Producto::getEstado,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .toList();
    }

    @Override
    public List<Object[]> obtenerProductosPorUsuario() {
        return productoRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Producto::getUsuarioCampesino,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .toList();
    }

}
