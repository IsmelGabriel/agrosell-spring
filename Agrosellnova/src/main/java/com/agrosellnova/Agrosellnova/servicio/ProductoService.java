package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> obtenerTodosLosProductos();

    List<Producto> buscarProductosFiltrados(String nombre, String orden);
}
