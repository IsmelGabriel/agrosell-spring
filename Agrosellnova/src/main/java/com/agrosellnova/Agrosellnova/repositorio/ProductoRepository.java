package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findAllByOrderByIdDesc();
    List<Producto> findByNombreContainingIgnoreCaseOrderByPrecioAsc(String nombre);
    List<Producto> findByNombreContainingIgnoreCaseOrderByPrecioDesc(String nombre);
    List<Producto> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);
    List<Producto> findByNombreContainingIgnoreCaseOrderByIdDesc(String nombre);

    List<Producto> findByUsuarioCampesino(String usuarioCampesino);
    List<Producto> findByUsuarioCampesinoOrderByIdDesc(String usuarioCampesino);
    Producto findByIdAndUsuarioCampesino(Long id, String usuarioCampesino);
    List<Producto> findByNombreContainingIgnoreCaseAndUsuarioCampesino(String nombre, String usuarioCampesino);
    List<Producto> findByFechaCosechaAndUsuarioCampesino(LocalDate fechaCosecha, String usuarioCampesino);
    List<Producto> findByEstado(String estado);
    List<Producto> findByEstadoContainingIgnoreCase(String estado);

    @Query("SELECT p FROM Producto p WHERE p.estado = 'disponible' order by p.id DESC")
    List<Producto> findProductosDisponibles();

    @Query("SELECT p FROM Producto p WHERE p.estado = 'Proximo a salir' order by p.id DESC")
    List<Producto> findProductosParaReserva();

    List<Producto> findTop4ByOrderByPrecioAsc();
    List<Producto> findTop4ByOrderByStockDesc();
    List<Producto> findTop4ByOrderByFechaCosechaDesc();

    // ==== Agregados para Dashboard ====

    // Total de productos registrados
    @Query("SELECT COUNT(p) FROM Producto p")
    Long getTotalProductos();

    // Total de productos disponibles
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.estado = 'disponible'")
    Long getTotalProductosDisponibles();

    // Productos con bajo stock (ejemplo: <= 10 unidades)
    @Query("SELECT p FROM Producto p WHERE p.stock <= 10 ORDER BY p.stock ASC")
    List<Producto> getProductosConBajoStock();

    // Promedio de precios de los productos
    @Query("SELECT AVG(p.precio) FROM Producto p")
    Double getPromedioPrecioProductos();
}
