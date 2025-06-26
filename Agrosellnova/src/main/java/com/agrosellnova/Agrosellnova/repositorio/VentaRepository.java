package com.agrosellnova.Agrosellnova.repositorio;


import com.agrosellnova.Agrosellnova.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByVendedor_NombreUsuario(String nombreUsuario);
}
