package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.DetalleFactura;
import com.agrosellnova.Agrosellnova.modelo.Factura;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {

    // Buscar todos los detalles de una factura
    List<DetalleFactura> findByFactura(Factura factura);

    // Buscar detalles por ID de factura
    @Query("SELECT d FROM DetalleFactura d WHERE d.factura.idFactura = :idFactura")
    List<DetalleFactura> findByFacturaId(Long idFactura);

    // Buscar detalles por producto
    List<DetalleFactura> findByProducto(Producto producto);

    // Obtener total de ventas de un producto
    @Query("SELECT SUM(d.cantidad) FROM DetalleFactura d WHERE d.producto.id = :idProducto")
    Double getTotalVentasByProducto(Long idProducto);

    // Eliminar todos los detalles de una factura
    void deleteByFactura(Factura factura);
}
