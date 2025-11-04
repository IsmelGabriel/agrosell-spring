package com.agrosellnova.Agrosellnova.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Bienvenido a AgroSell Nova");
        message.setText("Hola " + username + ",\n\n¡Gracias por registrarte en AgroSell Nova! 🌱\n\n" +
                "Ahora puedes acceder a todos nuestros servicios.\n\n" +
                "Saludos,\nEl equipo de AgroSell Nova");

        mailSender.send(message);
    }

    public void sendCustomEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendRoleUpdateEmail(String to, String username, String nuevoRol) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Actualización de rol en AgroSell Nova");
        message.setText("Hola " + username + ",\n\n" +
                "Tu rol en AgroSell Nova ha sido actualizado a: " + nuevoRol + ".\n\n" +
                "Si tienes dudas, por favor comunícate con soporte.");
        mailSender.send(message);
    }

    public void sendEstadoUpdateEmail(String to, String username, String nuevoEstado) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Actualización de estado en AgroSell Nova");
        message.setText("Hola " + username + ",\n\n" +
                "Tu estado en AgroSell Nova ha sido actualizado a: " + nuevoEstado + ".\n\n" +
                "Si tienes dudas, comunícate con nuestro equipo de soporte.");
        mailSender.send(message);
    }

    public void sendAcceptedProducerEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("¡Felicidades! Eres un productor aceptado en AgroSell Nova");
        message.setText("Hola " + username + ",\n\n" +
                "¡Felicidades! Tu solicitud para ser productor en AgroSell Nova ha sido aceptada. 🌾\n\n" +
                "Ahora puedes comenzar a publicar tus productos y llegar a más clientes.\n\n" +
                "Saludos,\nEl equipo de AgroSell Nova");

        mailSender.send(message);
    }

    public void sendRejectedProducerEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Actualización sobre tu solicitud de productor en AgroSell Nova");
        message.setText("Hola " + username + ",\n\n" +
                "Lamentamos informarte que tu solicitud para ser productor en AgroSell Nova ha sido rechazada. ❌\n\n" +
                "\n\n" +
                "Si tienes alguna pregunta o deseas más información, no dudes en contactarnos.\n\n" +
                "Saludos,\nEl equipo de AgroSell Nova");

        mailSender.send(message);
    }

    public void sendProducerApplicationEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Solicitud de productor recibida en AgroSell Nova");
        message.setText("Hola " + username + ",\n\n" +
                "Hemos recibido tu solicitud para ser productor en AgroSell Nova. 🌱\n\n" +
                "Nuestro equipo revisará tu solicitud y te notificaremos una vez que haya sido procesada.\n\n" +
                "Gracias por tu interés en unirte a nuestra comunidad.\n\n" +
                "Saludos,\nEl equipo de AgroSell Nova");

        mailSender.send(message);
    }


    // Enviar a un solo correo
    public void sendNewProductNotification(String to, String productName, Double productPrice) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Nuevo producto disponible en AgroSell Nova");
        message.setText("Hola,\n\n" +
                "¡Tenemos un nuevo producto disponible en AgroSell Nova! 🌟\n\n" +
                "Producto: " + productName + "\n" +
                "Precio: " + productPrice + "\n\n" +
                "Visita nuestra plataforma para más detalles y realizar tu compra.\n\n" +
                "Saludos,\nEl equipo de AgroSell Nova");

        mailSender.send(message);
    }

    // Enviar a todos los clientes (asíncrono para no bloquear la publicación del producto)
    @Async("taskExecutor")
    public void sendNewProductNotificationToAll(List<String> correos, String productName, Double ProductPrice) {
        for (String correo : correos) {
            sendNewProductNotification(correo, productName, ProductPrice);
        }
    }

    public void sendBookingConfirmationEmail(String to, String username, String producto, String bookingDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmación de reserva en AgroSell Nova");
        message.setText("Hola " + username + ",\n\n" +
                "Tu reserva para el producto '" + producto + "' ha sido confirmada para la fecha: " + bookingDate + ". 📅\n\n" +
                "Gracias por confiar en AgroSell Nova.\n\n" +
                "Saludos,\nEl equipo de AgroSell Nova");

        mailSender.send(message);
    }

    public void sendPaymentConfirmationEmail(String to, String username, String paymentDate, Double amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmación de pago en AgroSell Nova");
        message.setText("Hola " + username + ",\n\n" +
                "Tu pago de $" + amount + " ha sido recibido con éxito el " + paymentDate + ". 💳\n\n" +
                "Gracias por tu compra en AgroSell Nova.\n\n" +
                "Saludos,\nEl equipo de AgroSell Nova");

        mailSender.send(message);
    }

    public void sendResponsePqrsEmail(String to, String username, String respuesta) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Respuesta a tu PQRS en AgroSell Nova");
        message.setText("Hola " + username + ",\n\n" +
                "Hemos respondido a tu PQRS:\n\n" +
                respuesta + "\n\n" +
                "Gracias por contactarnos.\n\n" +
                "Saludos,\nEl equipo de AgroSell Nova");

        mailSender.send(message);
    }

}
