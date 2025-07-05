package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.repositorio.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public void guardarVenta(Venta venta) {
        venta.setFechaVenta(LocalDate.now());
        ventaRepository.save(venta);
    }

    @Override
    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }


    @Override
    public List<Venta> obtenerVentasPorProductor(String productor) {
        return ventaRepository.findByVendedor_NombreUsuario(productor);
    }

    @Override
    public List<Venta> filtrarVentas(String productor, String criterio, String valor) {
        switch (criterio.toLowerCase()) {
            case "id":
                try {
                    Long id = Long.parseLong(valor);
                    return ventaRepository
                            .findByIdVentaAndVendedor_NombreUsuario(id, productor);
                } catch (NumberFormatException e) {
                    return Collections.emptyList();
                }

            case "producto":
                return ventaRepository
                        .findByVendedor_NombreUsuarioAndProducto_NombreContainingIgnoreCase(productor, valor);

            case "fecha":
                try {
                    LocalDate fecha = LocalDate.parse(valor);
                    return ventaRepository
                            .findByVendedor_NombreUsuarioAndFechaVenta(productor, fecha);
                } catch (DateTimeParseException e) {
                    return Collections.emptyList();
                }

            case "comprador":
                return ventaRepository
                        .findByVendedor_NombreUsuarioAndComprador_NombreUsuarioContainingIgnoreCase(productor, valor);

            default:
                return Collections.emptyList();
        }
    }

    @Override
    public List<Venta> findByComprador_NombreUsuario(String nombreUsuario) {
        List<Venta> compras = ventaRepository.findByComprador_NombreUsuario(nombreUsuario);
        return compras;
    }

    @Override
    public List<Venta> filtrarCompras(String comprador, String criterio, String valor) {
        switch (criterio.toLowerCase()) {
            case "id":
                try {
                    Long id = Long.parseLong(valor);
                    return ventaRepository.findByIdVentaAndComprador_NombreUsuario(id, comprador);
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "producto":
                return ventaRepository.findByComprador_NombreUsuarioAndProducto_NombreContainingIgnoreCase(comprador, valor);
            case "fecha":
                try {
                    LocalDate fecha = LocalDate.parse(valor);
                    return ventaRepository.findByComprador_NombreUsuarioAndFechaVenta(comprador, fecha);
                } catch (DateTimeParseException e) {
                    return List.of();
                }
            case "vendedor":
                return ventaRepository.findByComprador_NombreUsuarioAndVendedor_NombreUsuarioContainingIgnoreCase(comprador, valor);
            default:
                return List.of();
        }
    }

}
