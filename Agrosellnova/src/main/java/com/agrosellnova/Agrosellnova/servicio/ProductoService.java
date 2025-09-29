package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Producto;

import java.util.List;

public interface ProductoService {

    // CRUD básico
    void guardarProducto(Producto producto);
    Producto obtenerPorId(Long id);
    void actualizarProducto(Producto producto);

    // Listados
    List<Producto> obtenerTodosLosProductos();
    List<Producto> obtenerProductosPorUsuario(String usuarioCampesino);
    List<Producto> obtenerProductosPorProductor(String usuarioCampesino);
    List<Producto> obtenerProductosPorEstado(String estado);
    List<Producto> obtenerProductosDisponibles();
    List<Producto> obtenerProductosParaReserva();

    // Filtros
    List<Producto> buscarProductosFiltrados(String nombre, String orden);
    List<Producto> filtrarProductos(String usuario, String criterio, String valor);

    // Métricas para dashboard
    Long obtenerCantidadTotalProductos();
    Long obtenerCantidadProductosDisponibles();
    Long obtenerCantidadProductosPorEstado(String estado);

    // Gráficos
    List<Object[]> obtenerProductosPorEstado(); // estado - cantidad
    List<Object[]> obtenerProductosPorUsuario(); // campesino - cantidad

}

