package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
