package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Calificaciones;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionesRepository extends JpaRepository<Calificaciones, Long> {
    // Buscar calificaciones por producto
    List<Calificaciones> findByProducto(Producto producto);

    // Buscar calificaciones por usuario
    List<Calificaciones> findByUsuarioId(Long usuarioId);
    List<Calificaciones>findByProductoId(Long productoId);

    //peticion
    @Query("SELECT AVG(c.calificacion) FROM Calificaciones c WHERE c.producto.id = :productoId")
    Double ObtenerPromedioByProducto(@Param("productoId")Long productoId);

    @Query("SELECT COUNT(c) FROM Calificaciones c WHERE c.producto.id = :productoId")
    long contarCalificacionesPorProducto(@Param("productoId") Long productoId);


}
