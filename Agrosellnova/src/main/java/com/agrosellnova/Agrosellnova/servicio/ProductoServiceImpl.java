package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void guardarProducto(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    public List<Producto> listarProductosPorUsuario(String usuario) {
        return productoRepository.findByUsuarioCampesino(usuario);
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
    public List<Producto> obtenerProductosPorUsuario(String usuarioCampesino) {
        return productoRepository.findByUsuarioCampesino(usuarioCampesino);
    }

    @Override
    public List<Producto> obtenerProductosPorProductor(String usuario) {
        List<Producto> productos = productoRepository.findByUsuarioCampesino(usuario);
        System.out.println("Productos encontrados: " + productos.size());
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
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Override
    public void actualizarProducto(Producto producto) {
        productoRepository.save(producto);
    }
}
