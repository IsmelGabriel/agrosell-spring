package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Puedes agregar métodos personalizados si los necesitas más adelante
}
