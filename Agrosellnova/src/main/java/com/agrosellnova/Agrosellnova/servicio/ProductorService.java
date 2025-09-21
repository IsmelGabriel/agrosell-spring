package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Productor;
import com.agrosellnova.Agrosellnova.repositorio.ProductorRepository;
import com.agrosellnova.Agrosellnova.repositorio.ResenaRepository;
import com.agrosellnova.Agrosellnova.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductorService {

    @Autowired
    private ProductorRepository productorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Crear nueva solicitud de productor
     */
    public Productor crearSolicitudProductor(Productor productor) {
        // Verificar que el usuario no tenga ya una solicitud
        if (yaEsProductor(productor.getIdUsuario())) {
            throw new RuntimeException("El usuario ya tiene una solicitud de productor registrada");
        }

        // Verificar que el usuario existe
        if (!usuarioRepository.existsById(Long.valueOf(productor.getIdUsuario()))) {
            throw new RuntimeException("El usuario no existe");
        }

        // Establecer valores por defecto
        productor.setEstadoSolicitud(Productor.EstadoSolicitud.Pendiente);
        productor.setFechaRegistro(LocalDateTime.now());
        productor.setFechaActualizacion(LocalDateTime.now());

        return productorRepository.save(productor);
    }

    /**
     * Obtener solicitud por ID de usuario
     */
    public Optional<Productor> obtenerPorUsuario(Integer idUsuario) {
        return productorRepository.findByIdUsuario(idUsuario);
    }

    /**
     * Verificar si un usuario ya es productor o tiene solicitud
     */
    public boolean yaEsProductor(Integer idUsuario) {
        return productorRepository.existsByIdUsuario(idUsuario);
    }

    /**
     * Obtener todas las solicitudes por estado
     */
    public List<Productor> obtenerPorEstado(Productor.EstadoSolicitud estado) {
        return productorRepository.findByEstadoSolicitudOrderByFechaRegistroDesc(estado);
    }

    /**
     * Obtener todos los productores
     */
    public List<Productor> obtenerTodos() {
        return productorRepository.findAll();
    }

    /**
     * Obtener solicitudes pendientes
     */
    public List<Productor> obtenerSolicitudesPendientes() {
        return obtenerPorEstado(Productor.EstadoSolicitud.Pendiente);
    }

    /**
     * Obtener productores aprobados
     */
    public List<Productor> obtenerProductoresAprobados() {
        return obtenerPorEstado(Productor.EstadoSolicitud.Aprobado);
    }

    /**
     * Aprobar solicitud de productor
     */
    public Productor aprobarSolicitud(Long idProductor) {
        Optional<Productor> productorOpt = productorRepository.findById(idProductor);
        if (productorOpt.isEmpty()) {
            throw new RuntimeException("Solicitud de productor no encontrada");
        }

        Productor productor = productorOpt.get();
        productor.setEstadoSolicitud(Productor.EstadoSolicitud.Aprobado);
        productor.setFechaActualizacion(LocalDateTime.now());

        return productorRepository.save(productor);
    }

    /**
     * Rechazar solicitud de productor
     */
    public Productor rechazarSolicitud(Long idProductor) {
        Optional<Productor> productorOpt = productorRepository.findById(idProductor);
        if (productorOpt.isEmpty()) {
            throw new RuntimeException("Solicitud de productor no encontrada");
        }

        Productor productor = productorOpt.get();
        productor.setEstadoSolicitud(Productor.EstadoSolicitud.Rechazado);
        productor.setFechaActualizacion(LocalDateTime.now());

        return productorRepository.save(productor);
    }

    /**
     * Buscar productores por nombre de finca
     */
    public List<Productor> buscarPorNombreFinca(String nombreFinca) {
        return productorRepository.findByNombreFincaContainingIgnoreCase(nombreFinca);
    }

    /**
     * Buscar productores por ubicación
     */
    public List<Productor> buscarPorUbicacion(String ubicacion) {
        return productorRepository.findByUbicacionContainingIgnoreCase(ubicacion);
    }

    /**
     * Buscar productores por tipo de producción
     */
    public List<Productor> buscarPorTipoProduccion(Productor.TipoProduccion tipoProduccion) {
        return productorRepository.findByTipoProduccion(tipoProduccion);
    }

    /**
     * Buscar productores por productos
     */
    public List<Productor> buscarPorProductos(String productos) {
        return productorRepository.findByProductosContainingIgnoreCase(productos);
    }

    /**
     * Obtener estadísticas
     */
    public long contarPorEstado(Productor.EstadoSolicitud estado) {
        return productorRepository.countByEstadoSolicitud(estado);
    }

    /**
     * Actualizar información de productor
     */
    public Productor actualizarProductor(Productor productor) {
        if (!productorRepository.existsById(productor.getIdProductor())) {
            throw new RuntimeException("El productor no existe");
        }

        productor.setFechaActualizacion(LocalDateTime.now());
        return productorRepository.save(productor);
    }

    /**
     * Obtener productor por ID
     */
    public Optional<Productor> obtenerPorId(Long idProductor) {
        return productorRepository.findById(idProductor);
    }

    /**
     * Eliminar solicitud de productor
     */
    public void eliminarSolicitud(Long idProductor) {
        if (!productorRepository.existsById(idProductor)) {
            throw new RuntimeException("Solicitud de productor no encontrada");
        }

        productorRepository.deleteById(idProductor);
    }
}
