package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.*;

import java.util.*;

import com.agrosellnova.Agrosellnova.repositorio.DetalleFacturaRepository;
import com.agrosellnova.Agrosellnova.repositorio.FacturaRepository;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private DetalleFacturaRepository detalleFacturaRepository;

    @Autowired
    private ProductoRepository productoRepository;


    public String generarNumeroFactura() {
        List<Factura> ultimaFactura = facturaRepository.findTopByOrderByIdFacturaDesc();

        if (ultimaFactura.isEmpty()) {
            return "FAC-" + LocalDate.now().getYear() + "-00001";
        }

        String ultimoNumero = ultimaFactura.get(0).getNumeroFactura();
        String[] partes = ultimoNumero.split("-");
        int consecutivo = Integer.parseInt(partes[2]) + 1;

        return "FAC-" + LocalDate.now().getYear() + "-" + String.format("%05d", consecutivo);
    }


    @Transactional
    public Factura crearFacturaDesdeCarrito(Usuario usuario, Pago pago, List<Map<String, Object>> carrito) {
        Factura factura = new Factura();
        factura.setNumeroFactura(generarNumeroFactura());
        factura.setUsuario(usuario);
        factura.setPago(pago);
        factura.setFechaEmision(LocalDate.now());
        factura.setFecha(LocalDate.now());
        factura.setEstadoFactura("PAGADA");

        if ("Efectivo".equals(pago.getMetodoPago()))
        {
            factura.setEstadoFactura("PENDIENTE");
        }
        double subtotalTotal = 0.0;
        List<DetalleFactura> detalles = new ArrayList<>();

        for (Map<String, Object> item : carrito) {
            Long idProducto = Long.valueOf(item.get("id").toString());
            Double cantidad = Double.valueOf(item.get("cantidad").toString());

            Producto producto = productoRepository.findById(idProducto).orElse(null);

            if (producto != null) {
                DetalleFactura detalle = new DetalleFactura();
                detalle.setProducto(producto);
                detalle.setNombreProducto(producto.getNombre());
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(producto.getPrecio());

                double subtotalDetalle = cantidad * producto.getPrecio();
                detalle.setSubtotal(subtotalDetalle);

                detalles.add(detalle);
                subtotalTotal += subtotalDetalle;
            }
        }

        factura.setSubtotal(subtotalTotal);
        factura.setDescuento(0.0);
        factura.setImpuesto(0.0);
        factura.setTotal(subtotalTotal);
        factura.setDetalle("Factura generada automáticamente - Compra de productos");


        factura = facturaRepository.save(factura);


        for (DetalleFactura detalle : detalles) {
            detalle.setFactura(factura);
            detalleFacturaRepository.save(detalle);
        }

        return factura;
    }


    @Transactional
    public Factura guardarFactura(Factura factura) {
        return facturaRepository.save(factura);
    }


    public Optional<Factura> buscarPorId(Long id) {
        return facturaRepository.findById(id);
    }


    public Optional<Factura> buscarPorNumero(String numeroFactura) {
        return facturaRepository.findByNumeroFactura(numeroFactura);
    }


    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }


    public List<Factura> listarPorUsuario(Usuario usuario) {
        return facturaRepository.findByUsuarioOrderByFechaEmisionDesc(usuario);
    }


    public List<Factura> listarPorEstado(String estado) {
        return facturaRepository.findByEstadoFactura(estado);
    }


    public List<Factura> listarPorUsuarioYEstado(Usuario usuario, String estado) {
        return facturaRepository.findByUsuarioAndEstadoFactura(usuario, estado);
    }


    @Transactional
    public Factura cambiarEstado(Long idFactura, String nuevoEstado) {
        Optional<Factura> facturaOpt = facturaRepository.findById(idFactura);

        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();
            factura.setEstadoFactura(nuevoEstado);
            return facturaRepository.save(factura);
        }

        return null;
    }


    @Transactional
    public boolean anularFactura(Long idFactura) {
        Optional<Factura> facturaOpt = facturaRepository.findById(idFactura);

        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();
            factura.setEstadoFactura("ANULADA");
            facturaRepository.save(factura);
            return true;
        }

        return false;
    }


    public List<Factura> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return facturaRepository.findByFechaEmisionBetween(fechaInicio, fechaFin);
    }


    public List<DetalleFactura> obtenerDetalles(Long idFactura) {
        return detalleFacturaRepository.findByFacturaId(idFactura);
    }


    public Long contarPorEstado(String estado) {
        return facturaRepository.countByEstadoFactura(estado);
    }


    @Transactional
    public boolean eliminarFactura(Long idFactura) {
        if (facturaRepository.existsById(idFactura)) {
            facturaRepository.deleteById(idFactura);
            return true;
        }
        return false;
    }
}

