package com.agrosellnova.Agrosellnova.servicio;


import com.agrosellnova.Agrosellnova.modelo.ConsultaSipsa;
import com.agrosellnova.Agrosellnova.repositorio.ConsultaSipsaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Servicio para gestionar las consultas a la API SIPSA
 * Coordina la consulta al servicio SOAP y el almacenamiento en BD
 */
@Service
public class SipsaService {

    @Autowired
    private SipsaSoapClient soapClient;

    @Autowired
    private ConsultaSipsaRepository consultaRepository;

    /**
     * Consulta precios por ciudad y los guarda en la base de datos
     * @return Lista de consultas guardadas
     */
    @Transactional
    public List<ConsultaSipsa> consultarYGuardarPreciosCiudad() {
        List<Map<String, Object>> datos = soapClient.consultarPreciosCiudad();

        for (Map<String, Object> dato : datos) {
            try {
                ConsultaSipsa consulta = new ConsultaSipsa();
                consulta.setCiudad((String) dato.get("ciudad"));
                consulta.setProducto((String) dato.get("producto"));
                consulta.setCodigoProducto((Integer) dato.get("codProducto"));
                consulta.setPrecioPromedio((Double) dato.get("precioPromedio"));
                consulta.setTipoConsulta("ciudad");

                // Parsear fecha de captura
                String fechaStr = (String) dato.get("fechaCaptura");
                if (fechaStr != null && !fechaStr.isEmpty()) {
                    try {
                        LocalDateTime fechaCaptura = LocalDateTime.parse(
                                fechaStr.substring(0, 19),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        );
                        consulta.setFechaCaptura(fechaCaptura);
                    } catch (Exception e) {
                        consulta.setFechaCaptura(LocalDateTime.now());
                    }
                }

                consultaRepository.save(consulta);
            } catch (Exception e) {
                System.err.println("Error guardando consulta: " + e.getMessage());
            }
        }

        return consultaRepository.findTop10ByOrderByFechaConsultaDesc();
    }

    /**
     * Consulta datos mensuales y los guarda
     * @return Lista de consultas guardadas
     */
    @Transactional
    public List<ConsultaSipsa> consultarYGuardarDatosMensuales() {
        List<Map<String, Object>> datos = soapClient.consultarDatosMensuales();

        for (Map<String, Object> dato : datos) {
            try {
                ConsultaSipsa consulta = new ConsultaSipsa();
                consulta.setProducto((String) dato.get("artiNombre"));
                consulta.setCodigoProducto((Integer) dato.get("artiId"));
                consulta.setFuente((String) dato.get("fuenNombre"));
                consulta.setCantidadKg((Double) dato.get("promedioKg"));
                consulta.setTipoConsulta("mensual");

                // Parsear fecha
                String fechaStr = (String) dato.get("fechaMesIni");
                if (fechaStr != null && !fechaStr.isEmpty()) {
                    try {
                        LocalDateTime fecha = LocalDateTime.parse(
                                fechaStr.substring(0, 19),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        );
                        consulta.setFechaCaptura(fecha);
                    } catch (Exception e) {
                        consulta.setFechaCaptura(LocalDateTime.now());
                    }
                }

                consultaRepository.save(consulta);
            } catch (Exception e) {
                System.err.println("Error guardando consulta mensual: " + e.getMessage());
            }
        }

        return consultaRepository.findTop10ByOrderByFechaConsultaDesc();
    }

    /**
     * Consulta datos semanales y los guarda
     * @return Lista de consultas guardadas
     */
    @Transactional
    public List<ConsultaSipsa> consultarYGuardarDatosSemanales() {
        List<Map<String, Object>> datos = soapClient.consultarDatosSemanales();

        for (Map<String, Object> dato : datos) {
            try {
                ConsultaSipsa consulta = new ConsultaSipsa();
                consulta.setProducto((String) dato.get("artiNombre"));
                consulta.setCodigoProducto((Integer) dato.get("artiId"));
                consulta.setFuente((String) dato.get("fuenNombre"));
                consulta.setCantidadKg((Double) dato.get("promedioKg"));
                consulta.setTipoConsulta("semanal");

                // Parsear fecha
                String fechaStr = (String) dato.get("fechaIni");
                if (fechaStr != null && !fechaStr.isEmpty()) {
                    try {
                        LocalDateTime fecha = LocalDateTime.parse(
                                fechaStr.substring(0, 19),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        );
                        consulta.setFechaCaptura(fecha);
                    } catch (Exception e) {
                        consulta.setFechaCaptura(LocalDateTime.now());
                    }
                }

                consultaRepository.save(consulta);
            } catch (Exception e) {
                System.err.println("Error guardando consulta semanal: " + e.getMessage());
            }
        }

        return consultaRepository.findTop10ByOrderByFechaConsultaDesc();
    }

    /**
     * Obtiene todas las consultas almacenadas
     */
    public List<ConsultaSipsa> obtenerTodasLasConsultas() {
        return consultaRepository.findAllByOrderByFechaConsultaDesc();
    }

    /**
     * Obtiene las últimas 20 consultas
     */
    public List<ConsultaSipsa> obtenerUltimasConsultas() {
        return consultaRepository.findTop10ByOrderByFechaConsultaDesc();
    }

    /**
     * Busca consultas por producto
     */
    public List<ConsultaSipsa> buscarPorProducto(String producto) {
        return consultaRepository.findByProductoContainingIgnoreCaseOrderByFechaConsultaDesc(producto);
    }

    /**
     * Busca consultas por ciudad
     */
    public List<ConsultaSipsa> buscarPorCiudad(String ciudad) {
        return consultaRepository.findByCiudadOrderByFechaConsultaDesc(ciudad);
    }

    /**
     * Busca por tipo de consulta
     */
    public List<ConsultaSipsa> buscarPorTipoConsulta(String tipo) {
        return consultaRepository.findByTipoConsultaOrderByFechaConsultaDesc(tipo);
    }

    /**
     * Consulta datos de abastecimiento y los guarda
     * @return Lista de consultas guardadas
     */
    @Transactional
    public List<ConsultaSipsa> consultarYGuardarAbastecimiento() {
        List<Map<String, Object>> datos = soapClient.consultarAbastecimiento();

        for (Map<String, Object> dato : datos) {
            try {
                ConsultaSipsa consulta = new ConsultaSipsa();
                consulta.setProducto((String) dato.get("artiNombre"));
                consulta.setCodigoProducto((Integer) dato.get("artiId"));
                consulta.setFuente((String) dato.get("fuenNombre"));
                consulta.setCantidadKg((Double) dato.get("cantidadTon"));
                consulta.setTipoConsulta("abastecimiento");

                // Parsear fecha
                String fechaStr = (String) dato.get("fechaMesIni");
                if (fechaStr != null && !fechaStr.isEmpty()) {
                    try {
                        LocalDateTime fecha = LocalDateTime.parse(
                                fechaStr.substring(0, 19),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        );
                        consulta.setFechaCaptura(fecha);
                    } catch (Exception e) {
                        consulta.setFechaCaptura(LocalDateTime.now());
                    }
                }

                consultaRepository.save(consulta);
            } catch (Exception e) {
                System.err.println("Error guardando consulta de abastecimiento: " + e.getMessage());
            }
        }

        return consultaRepository.findTop10ByOrderByFechaConsultaDesc();
    }
}
