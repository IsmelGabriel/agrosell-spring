package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.repositorio.ProductoRepository;
import com.agrosellnova.Agrosellnova.repositorio.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public void guardarVenta(Venta venta) {
        venta.setFechaVenta(LocalDate.now());

        if (venta.getProducto() != null) {
            Long productoId = venta.getProducto().getId();

            productoRepository.findById(productoId).ifPresentOrElse(productoBD -> {
                int stockActual = productoBD.getStock();
                int cantidadVendida = venta.getCantidadKg().intValue();

                if (stockActual >= cantidadVendida) {
                    productoBD.setStock(stockActual - cantidadVendida);
                    productoRepository.save(productoBD);
                    ventaRepository.save(venta);
                    emailService.sendPaymentConfirmationEmail(venta.getComprador().getCorreo(), venta.getComprador().getNombreUsuario(), venta.getFechaVenta().toString(), venta.getTotalVenta());
                } else {
                    throw new RuntimeException("Stock insuficiente para realizar la venta. Stock disponible: "
                            + stockActual + ", solicitado: " + cantidadVendida);
                }
            }, () -> {
                throw new RuntimeException("Producto no encontrado en la base de datos.");
            });

        } else {
            throw new RuntimeException("No se ha asignado ningún producto a la venta.");
        }
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

    public List<Venta> filtrarVentasAdmin(String criterio, String valor) {
        switch (criterio.toLowerCase()) {
            case "cliente":
                return ventaRepository.findByComprador_NombreUsuarioContainingIgnoreCase(valor);
            case "vendedor":
                return ventaRepository.findByVendedor_NombreUsuarioContainingIgnoreCase(valor);
            case "producto":
                return ventaRepository.findByProducto_NombreContainingIgnoreCase(valor);
            case "fecha":
                try {
                    LocalDate fecha = LocalDate.parse(valor);
                    return ventaRepository.findByFechaVenta(fecha);
                } catch (DateTimeParseException e) {
                    return List.of();
                }
            default:
                return List.of();
        }
    }

    @Override
    public List<Venta> obtenerComprasPorUsuario(String documentoUsuario) {
        return List.of();
    }

    @Override
    public List<Venta> sobtenerComprasPorUsuario(String documentoUsuario) {
        return List.of();
    }

    @Override
    public List<Venta> obtenerTodas() {
        return List.of();
    }


    @Override
    public Long obtenerTotalVentas() {
        return ventaRepository.findAll()
                .stream()
                .mapToLong(v -> v.getTotalVenta() != null ? v.getTotalVenta().longValue() : 0)
                .sum();
    }

    @Override
    public Long obtenerCantidadProductosVendidos() {
        return ventaRepository.findAll()
                .stream()
                .mapToLong(v -> v.getCantidadKg() != null ? v.getCantidadKg().longValue() : 0)
                .sum();
    }

    @Override
    public Long obtenerCantidadClientes() {
        return ventaRepository.findAll()
                .stream()
                .map(v -> v.getComprador().getNombreUsuario())
                .distinct()
                .count();
    }

    @Override
    public List<Object[]> obtenerVentasPorMes() {
        return ventaRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        v -> v.getFechaVenta().getMonth(),
                        Collectors.summingDouble(Venta::getTotalVenta)
                ))
                .entrySet()
                .stream()
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .toList();
    }

    @Override
    public List<Object[]> obtenerProductosMasVendidos() {
        return ventaRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        v -> v.getProducto().getNombre(),
                        Collectors.summingDouble(Venta::getCantidadKg)
                ))
                .entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .limit(5) // top 5
                .toList();
    }

    public List<Object[]> obtenerVentasMensuales() {
        return ventaRepository.obtenerVentasMensuales();
    }


}
