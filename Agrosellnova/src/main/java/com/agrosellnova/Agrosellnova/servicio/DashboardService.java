package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import com.agrosellnova.Agrosellnova.repositorio.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private VentaRepository ventaRepository;

    // Total de ventas ($)
    public double obtenerTotalVentas() {
        Double total = ventaRepository.totalVentas();
        return total != null ? total : 0.0;
    }

    // Total de productos vendidos (kg)
    public double obtenerTotalProductosVendidos() {
        Double total = ventaRepository.totalProductosVendidos();
        return total != null ? total : 0.0;
    }

    // Total de clientes
    public long obtenerTotalClientes() {
        Long total = ventaRepository.totalClientes();
        return total != null ? total : 0L;
    }

    // Ventas recientes (últimas 4)
    public List<Venta> obtenerVentasRecientes() {
        return ventaRepository.findTop4ByOrderByIdVentaDesc();
    }
}
