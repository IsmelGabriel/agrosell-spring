package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Productor;
import com.agrosellnova.Agrosellnova.modelo.Usuario;
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

    @Autowired
    private EmailService emailService;

    public Productor crearOActualizarSolicitudProductor(Productor productor) {

        Optional<Productor> solicitudExistente = productorRepository.findByIdUsuario(productor.getIdUsuario());

        if (solicitudExistente.isPresent()) {
            Productor productorExistente = solicitudExistente.get();


            if (productorExistente.getEstadoSolicitud() == Productor.EstadoSolicitud.Rechazado) {

                productorExistente.setNombreFinca(productor.getNombreFinca());
                productorExistente.setUbicacion(productor.getUbicacion());
                productorExistente.setAreaCultivo(productor.getAreaCultivo());
                productorExistente.setTipoProduccion(productor.getTipoProduccion());
                productorExistente.setAnosExperiencia(productor.getAnosExperiencia());
                productorExistente.setCapacidadProduccion(productor.getCapacidadProduccion());
                productorExistente.setContactoComercial(productor.getContactoComercial());
                productorExistente.setProductos(productor.getProductos());
                productorExistente.setDescripcion(productor.getDescripcion());


                productorExistente.setEstadoSolicitud(Productor.EstadoSolicitud.Pendiente);
                productorExistente.setFechaActualizacion(LocalDateTime.now());

                emailService.sendProducerApplicationEmail(
                        usuarioRepository.findById(Long.valueOf(productor.getIdUsuario())).get().getCorreo(),
                        usuarioRepository.findById(Long.valueOf(productor.getIdUsuario())).get().getNombreUsuario()
                );
                return productorRepository.save(productorExistente);
            } else {

                throw new RuntimeException("Ya tienes una solicitud de productor " +
                        productorExistente.getEstadoSolicitud());
            }
        }


        if (!usuarioRepository.existsById(Long.valueOf(productor.getIdUsuario()))) {
            throw new RuntimeException("El usuario no existe");
        }


        productor.setEstadoSolicitud(Productor.EstadoSolicitud.Pendiente);
        productor.setFechaRegistro(LocalDateTime.now());
        productor.setFechaActualizacion(LocalDateTime.now());

        return productorRepository.save(productor);
    }
    public boolean puedeEnviarSolicitud(Integer idUsuario) {
        Optional<Productor> productor = productorRepository.findByIdUsuario(idUsuario);
        if (productor.isEmpty()) {
            return true;
        }

        return productor.get().getEstadoSolicitud() == Productor.EstadoSolicitud.Rechazado;
    }


    public Optional<Productor> obtenerPorUsuario(Integer idUsuario) {
        return productorRepository.findByIdUsuario(idUsuario);
    }


    public boolean yaEsProductor(Integer idUsuario) {
        return productorRepository.existsByIdUsuario(idUsuario);
    }


    public List<Productor> obtenerPorEstado(Productor.EstadoSolicitud estado) {
        return productorRepository.findByEstadoSolicitudOrderByFechaRegistroDesc(estado);
    }



    public List<Productor> obtenerTodos() {
        return productorRepository.findAll();
    }


    public List<Productor> obtenerSolicitudesPendientes() {
        return obtenerPorEstado(Productor.EstadoSolicitud.Pendiente);
    }


    public List<Productor> obtenerProductoresAprobados() {
        return obtenerPorEstado(Productor.EstadoSolicitud.Aprobado);
    }


    public Productor aprobarSolicitud(Long idProductor) {
        Optional<Productor> productorOpt = productorRepository.findById(idProductor);
        if (productorOpt.isEmpty()) {
            throw new RuntimeException("Solicitud de productor no encontrada");
        }

        Productor productor = productorOpt.get();
        productor.setEstadoSolicitud(Productor.EstadoSolicitud.Aprobado);
        productor.setFechaActualizacion(LocalDateTime.now());
        emailService.sendAcceptedProducerEmail(
                usuarioRepository.findById(Long.valueOf(productor.getIdUsuario())).get().getCorreo(),
                usuarioRepository.findById(Long.valueOf(productor.getIdUsuario())).get().getNombreUsuario()
        );

        return productorRepository.save(productor);
    }


    public Productor rechazarSolicitud(Long idProductor) {
        Optional<Productor> productorOpt = productorRepository.findById(idProductor);
        if (productorOpt.isEmpty()) {
            throw new RuntimeException("Solicitud de productor no encontrada");
        }

        Productor productor = productorOpt.get();
        productor.setEstadoSolicitud(Productor.EstadoSolicitud.Rechazado);
        productor.setFechaActualizacion(LocalDateTime.now());
        emailService.sendRejectedProducerEmail(
                usuarioRepository.findById(Long.valueOf(productor.getIdUsuario())).get().getCorreo(),
                usuarioRepository.findById(Long.valueOf(productor.getIdUsuario())).get().getNombreUsuario()
        );

        return productorRepository.save(productor);
    }


    public List<Productor> buscarPorNombreFinca(String nombreFinca) {
        return productorRepository.findByNombreFincaContainingIgnoreCase(nombreFinca);
    }


    public List<Productor> buscarPorUbicacion(String ubicacion) {
        return productorRepository.findByUbicacionContainingIgnoreCase(ubicacion);
    }


    public List<Productor> buscarPorTipoProduccion(Productor.TipoProduccion tipoProduccion) {
        return productorRepository.findByTipoProduccion(tipoProduccion);
    }


    public List<Productor> buscarPorProductos(String productos) {
        return productorRepository.findByProductosContainingIgnoreCase(productos);
    }


    public long contarPorEstado(Productor.EstadoSolicitud estado) {
        return productorRepository.countByEstadoSolicitud(estado);
    }


    public Productor actualizarProductor(Productor productor) {
        if (!productorRepository.existsById(productor.getIdProductor())) {
            throw new RuntimeException("El productor no existe");
        }

        productor.setFechaActualizacion(LocalDateTime.now());
        return productorRepository.save(productor);
    }


    public Optional<Productor> obtenerPorId(Long idProductor) {
        return productorRepository.findById(idProductor);
    }


    public void eliminarSolicitud(Long idProductor) {
        if (!productorRepository.existsById(idProductor)) {
            throw new RuntimeException("Solicitud de productor no encontrada");
        }

        productorRepository.deleteById(idProductor);
    }
}
