package com.agrosellnova.Agrosellnova.servicio.apiSipsa;


import com.agrosellnova.Agrosellnova.modelo.apiSipsa.*;
import com.agrosellnova.Agrosellnova.repositorio.apiSipsa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class SipsaServiceCompleto {

    @Autowired
    private SipsaSoapClient soapClient;

    @Autowired
    private SipsaDatoMensualRepository datoMensualRepository;

    @Autowired
    private SipsaDatoSemanalRepository datoSemanalRepository;

    @Autowired
    private SipsaAbastecimientoRepository abastecimientoRepository;


    @Transactional
    public List<SipsaDatoMensual> consultarYGuardarDatosMensuales() {
        List<Map<String, Object>> datos = soapClient.consultarDatosMensuales();

        LocalDateTime unMesAtras = LocalDateTime.now().minusMonths(6);
        eliminarDatosMensualesAntiguos(unMesAtras);

        int registrosGuardados = 0;
        int registrosDescartados = 0;
        for (Map<String, Object> dato : datos) {
            try {

                String fechaStr = (String) dato.get("fechaMesIni");
                LocalDateTime fechaMesInicio = null;

                if (fechaStr != null && !fechaStr.isEmpty()) {
                    fechaMesInicio = parsearFecha(fechaStr);
                }

                if (fechaMesInicio != null && fechaMesInicio.isAfter(unMesAtras)) {
                    SipsaDatoMensual mensual = new SipsaDatoMensual();
                    mensual.setProducto((String) dato.get("artiNombre"));
                    mensual.setCodigoProducto((Integer) dato.get("artiId"));
                    mensual.setFuente((String) dato.get("fuenNombre"));
                    mensual.setPromedioKg((Double) dato.get("promedioKg"));
                    mensual.setMinimoKg((Double) dato.get("minimoKg"));
                    mensual.setMaximoKg((Double) dato.get("maximoKg"));
                    mensual.setFechaMesInicio(fechaMesInicio);

                    datoMensualRepository.save(mensual);
                    registrosGuardados++;
                } else {
                    registrosDescartados++;
                }
                } catch (Exception e) {
            System.err.println("Error guardando dato mensual: " + e.getMessage());
        }
    }

        System.out.println("Datos mensuales: " + registrosGuardados + " guardados, " + registrosDescartados + " descartados (antiguos)");
        return datoMensualRepository.findRecientes(unMesAtras);
}

    public List<SipsaDatoMensual> obtenerDatosMensuales() {
        LocalDateTime unMesAtras = LocalDateTime.now().minusMonths(6);
        return datoMensualRepository.findRecientes(unMesAtras);
    }

    public List<SipsaDatoMensual> buscarDatosMensuales(String producto, String fuente,
                                                       LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        return datoMensualRepository.buscarConFiltros(producto, fuente, fechaDesde, fechaHasta);
    }

    @Transactional
    private void eliminarDatosMensualesAntiguos(LocalDateTime fecha) {
        List<SipsaDatoMensual> antiguos = datoMensualRepository.findAll()
                .stream()
                .filter(d -> d.getFechaMesInicio() != null && d.getFechaMesInicio().isBefore(fecha))
                .toList();
        datoMensualRepository.deleteAll(antiguos);
    }

    @Transactional
    public List<SipsaDatoSemanal> consultarYGuardarDatosSemanales() {
        List<Map<String, Object>> datos = soapClient.consultarDatosSemanales();

        LocalDateTime unMesAtras = LocalDateTime.now().minusMonths(1);
        eliminarDatosSemanalesAntiguos(unMesAtras);

        int registrosGuardados = 0;
        int registrosDescartados = 0;

        for (Map<String, Object> dato : datos) {
            try {
                String fechaStr = (String) dato.get("fechaIni");
                LocalDateTime fechaInicioSemana = null;

                if (fechaStr != null && !fechaStr.isEmpty()) {
                    fechaInicioSemana = parsearFecha(fechaStr);
                }

                if (fechaInicioSemana != null && fechaInicioSemana.isAfter(unMesAtras)) {
                    SipsaDatoSemanal semanal = new SipsaDatoSemanal();
                    semanal.setProducto((String) dato.get("artiNombre"));
                    semanal.setCodigoProducto((Integer) dato.get("artiId"));
                    semanal.setFuente((String) dato.get("fuenNombre"));
                    semanal.setPromedioKg((Double) dato.get("promedioKg"));
                    semanal.setMinimoKg((Double) dato.get("minimoKg"));
                    semanal.setMaximoKg((Double) dato.get("maximoKg"));
                    semanal.setFechaInicioSemana(fechaInicioSemana);

                    datoSemanalRepository.save(semanal);
                    registrosGuardados++;
                } else {
                    registrosDescartados++;
                }
            } catch (Exception e) {
                System.err.println("Error guardando dato semanal: " + e.getMessage());
            }
        }

        System.out.println("Datos semanales: " + registrosGuardados + " guardados, " + registrosDescartados + " descartados (antiguos)");
        return datoSemanalRepository.findRecientes(unMesAtras);
    }

    public List<SipsaDatoSemanal> obtenerDatosSemanales() {
        LocalDateTime unMesAtras = LocalDateTime.now().minusMonths(1);
        return datoSemanalRepository.findRecientes(unMesAtras);
    }

    public List<SipsaDatoSemanal> buscarDatosSemanales(String producto, String fuente,
                                                       LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        return datoSemanalRepository.buscarConFiltros(producto, fuente, fechaDesde, fechaHasta);
    }

    @Transactional
    private void eliminarDatosSemanalesAntiguos(LocalDateTime fecha) {
        List<SipsaDatoSemanal> antiguos = datoSemanalRepository.findAll()
                .stream()
                .filter(d -> d.getFechaInicioSemana() != null && d.getFechaInicioSemana().isBefore(fecha))
                .toList();
        datoSemanalRepository.deleteAll(antiguos);
    }


    @Transactional
    public List<SipsaAbastecimiento> consultarYGuardarAbastecimiento() {
        List<Map<String, Object>> datos = soapClient.consultarAbastecimiento();

        LocalDateTime unMesAtras = LocalDateTime.now().minusMonths(6);
        eliminarAbastecimientoAntiguo(unMesAtras);

        int registrosGuardados = 0;
        int registrosDescartados = 0;

        for (Map<String, Object> dato : datos) {
            try {

                String fechaStr = (String) dato.get("fechaMesIni");
                LocalDateTime fechaMesInicio = null;

                if (fechaStr != null && !fechaStr.isEmpty()) {
                    fechaMesInicio = parsearFecha(fechaStr);
                }

                if (fechaMesInicio != null && fechaMesInicio.isAfter(unMesAtras)) {
                    SipsaAbastecimiento abastecimiento = new SipsaAbastecimiento();
                    abastecimiento.setProducto((String) dato.get("artiNombre"));
                    abastecimiento.setCodigoProducto((Integer) dato.get("artiId"));
                    abastecimiento.setFuente((String) dato.get("fuenNombre"));
                    abastecimiento.setCantidadToneladas((Double) dato.get("cantidadTon"));
                    abastecimiento.setFechaMesInicio(fechaMesInicio);

                    abastecimientoRepository.save(abastecimiento);
                    registrosGuardados++;
                } else {
                    registrosDescartados++;
                }
            } catch (Exception e) {
                System.err.println("Error guardando abastecimiento: " + e.getMessage());
            }
        }

        System.out.println("Abastecimiento: " + registrosGuardados + " guardados, " + registrosDescartados + " descartados (antiguos)");
        return abastecimientoRepository.findRecientes(unMesAtras);
    }

    public List<SipsaAbastecimiento> obtenerAbastecimiento() {
        LocalDateTime unMesAtras = LocalDateTime.now().minusMonths(6);
        return abastecimientoRepository.findRecientes(unMesAtras);
    }

    public List<SipsaAbastecimiento> buscarAbastecimiento(String producto, String fuente,
                                                          LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        return abastecimientoRepository.buscarConFiltros(producto, fuente, fechaDesde, fechaHasta);
    }

    @Transactional
    private void eliminarAbastecimientoAntiguo(LocalDateTime fecha) {
        List<SipsaAbastecimiento> antiguos = abastecimientoRepository.findAll()
                .stream()
                .filter(a -> a.getFechaMesInicio() != null && a.getFechaMesInicio().isBefore(fecha))
                .toList();
        abastecimientoRepository.deleteAll(antiguos);
    }

    private LocalDateTime parsearFecha(String fechaStr) {
        try {
            return LocalDateTime.parse(
                    fechaStr.substring(0, 19),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
            );
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
