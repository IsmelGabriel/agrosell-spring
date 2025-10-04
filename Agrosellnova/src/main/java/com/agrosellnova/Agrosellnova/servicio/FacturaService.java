package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Factura;
import java.util.List;

public interface FacturaService {
    Factura guardarFactura(Factura factura);
    Factura obtenerFacturaPorId(Long id);
    List<Factura> listarFacturas();
    void eliminarFactura(Long id);


}

