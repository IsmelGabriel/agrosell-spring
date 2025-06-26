package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ProductoService {

    List<Producto> obtenerTodosLosProductos();

    List<Producto> buscarProductosFiltrados(String nombre, String orden);

    void guardarProducto(Producto producto);

    List<Producto> listarProductosPorUsuario(String usuario);

}
