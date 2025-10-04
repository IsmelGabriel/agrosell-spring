package com.agrosellnova.Agrosellnova.servicio;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionAsyncService {

    private final EmailService emailService;

    public NotificacionAsyncService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    public void enviarCorreosAsync(List<String> correos, String nombreProducto, Double precioProducto) {
        // delega al emailService (sin bloquear la petición)
        emailService.sendNewProductNotificationToAll(correos, nombreProducto, precioProducto);
    }
}
