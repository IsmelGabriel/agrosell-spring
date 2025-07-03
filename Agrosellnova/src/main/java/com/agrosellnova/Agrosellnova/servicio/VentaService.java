package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Venta;
import java.util.List;

public interface VentaService {
    List<Venta> obtenerTodasLasVentas();
    void guardarVenta(Venta venta);
    Venta obtenerVentaPorId(Long id);
    void eliminarVenta(Long id);
    List<Venta> obtenerVentasPorProductor(String productor);
    List<Venta> filtrarVentas(String productor, String criterio, String valor);
    }

