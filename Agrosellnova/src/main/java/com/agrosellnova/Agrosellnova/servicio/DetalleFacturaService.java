package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.DetalleFactura;
import com.agrosellnova.Agrosellnova.modelo.Factura;
import com.agrosellnova.Agrosellnova.modelo.Producto;
import com.agrosellnova.Agrosellnova.repositorio.DetalleFacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleFacturaService {

    @Autowired
    private DetalleFacturaRepository detalleFacturaRepository;

    // Guardar detalle de factura
    @Transactional
    public DetalleFactura guardarDetalle(DetalleFactura detalle) {
        return detalleFacturaRepository.save(detalle);
    }

    // Buscar detalle por ID
    public Optional<DetalleFactura> buscarPorId(Long id) {
        return detalleFacturaRepository.findById(id);
    }

    // Listar todos los detalles
    public List<DetalleFactura> listarTodos() {
        return detalleFacturaRepository.findAll();
    }

    // Listar detalles de una factura
    public List<DetalleFactura> listarPorFactura(Factura factura) {
        return detalleFacturaRepository.findByFactura(factura);
    }

    // Listar detalles por ID de factura
    public List<DetalleFactura> listarPorFacturaId(Long idFactura) {
        return detalleFacturaRepository.findByFacturaId(idFactura);
    }

    // Listar detalles por producto
    public List<DetalleFactura> listarPorProducto(Producto producto) {
        return detalleFacturaRepository.findByProducto(producto);
    }

    // Obtener total de ventas de un producto
    public Double obtenerTotalVentasProducto(Long idProducto) {
        Double total = detalleFacturaRepository.getTotalVentasByProducto(idProducto);
        return total != null ? total : 0.0;
    }

    // Eliminar detalle
    @Transactional
    public boolean eliminarDetalle(Long id) {
        if (detalleFacturaRepository.existsById(id)) {
            detalleFacturaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Eliminar todos los detalles de una factura
    @Transactional
    public void eliminarDetallesPorFactura(Factura factura) {
        detalleFacturaRepository.deleteByFactura(factura);
    }

    // Calcular subtotal de un detalle
    public Double calcularSubtotal(DetalleFactura detalle) {
        return detalle.getCantidad() * detalle.getPrecioUnitario();
    }

    // Actualizar detalle
    @Transactional
    public DetalleFactura actualizarDetalle(DetalleFactura detalle) {
        detalle.calcularSubtotal();
        return detalleFacturaRepository.save(detalle);
    }
}
