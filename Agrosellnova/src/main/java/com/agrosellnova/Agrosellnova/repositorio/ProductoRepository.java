package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findAllByOrderByIdDesc();

    List<Producto> findByNombreContainingIgnoreCaseOrderByPrecioAsc(String nombre);
    List<Producto> findByNombreContainingIgnoreCaseOrderByPrecioDesc(String nombre);
    List<Producto> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);
    List<Producto> findByNombreContainingIgnoreCaseOrderByIdDesc(String nombre);
    List<Producto> findByUsuarioCampesino(String usuarioCampesino);

}
