package com.agrosellnova.Agrosellnova.controladores;


import com.agrosellnova.Agrosellnova.modelo.Factura;
import com.agrosellnova.Agrosellnova.servicio.FacturaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @PostMapping
    public Factura crearFactura(@RequestBody Factura factura) {
        return facturaService.guardarFactura(factura);
    }

    @GetMapping("/{id}")
    public Factura obtenerFactura(@PathVariable Long id) {
        return facturaService.obtenerFacturaPorId(id);
    }

    @GetMapping
    public List<Factura> listarFacturas() {
        return facturaService.listarFacturas();
    }

    @DeleteMapping("/{id}")
    public void eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
    }
}
