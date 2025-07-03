package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.repositorio.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
        ventaRepository.save(venta);
    }

    @Override
    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public List<Venta> obtenerVentasPorVendedor(String nombreUsuario) {
        return ventaRepository.findByVendedor_NombreUsuario(nombreUsuario);
    }
    @Override
    public List<Venta> filtrarVentas(String criterio, String valor, String vendedor) {
        if (criterio == null || valor == null || valor.trim().isEmpty()) {
            return ventaRepository.findByVendedor_NombreUsuario(vendedor);
        }

        switch (criterio) {
            case "id":
                try {
                    Long id = Long.parseLong(valor);
                    return ventaRepository.findByIdVentaAndVendedor_NombreUsuario(id, vendedor);
                } catch (NumberFormatException e) {
                    return new ArrayList<>();
                }

            case "producto":
                return ventaRepository.findByProducto_NombreContainingIgnoreCaseAndVendedor_NombreUsuario(valor, vendedor);

            case "comprador":
                return ventaRepository.findByComprador_NombreUsuarioContainingIgnoreCaseAndVendedor_NombreUsuario(valor, vendedor);

            case "fecha":
                try {
                    LocalDate fecha = LocalDate.parse(valor);
                    return ventaRepository.findByFechaVentaAndVendedor_NombreUsuario(fecha, vendedor);
                } catch (DateTimeParseException e) {
                    return new ArrayList<>();
                }

            default:
                return ventaRepository.findByVendedor_NombreUsuario(vendedor);
        }
    }
}



