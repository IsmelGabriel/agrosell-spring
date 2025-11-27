package com.agrosellnova.Agrosellnova.servicio;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmailService {

    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    private void sendEmail(String to, String subject, String body) {
        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);

        Mail mail = new Mail(from, subject, toEmail, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException e) {
            System.err.println("Error enviando email a " + to + ": " + e.getMessage());
        }
    }

    @Async("taskExecutor")
    public void sendWelcomeEmail(String to, String username) {
        sendEmail(to, "Bienvenido a AgroSell Nova",
                "Hola " + username + ",\n\n¡Gracias por registrarte en AgroSell Nova! 🌱\n\nSaludos,\nEl equipo de AgroSell Nova");
    }

    @Async("taskExecutor")
    public void sendCustomEmail(String to, String subject, String body) {
        sendEmail(to, subject, body);
    }

    @Async("taskExecutor")
    public void sendRoleUpdateEmail(String to, String username, String nuevoRol) {
        sendEmail(to, "Actualización de rol en AgroSell Nova",
                "Hola " + username + ",\n\nTu rol ha sido actualizado a: " + nuevoRol + ".");
    }

    @Async("taskExecutor")
    public void sendEstadoUpdateEmail(String to, String username, String nuevoEstado) {
        sendEmail(to, "Actualización de estado en AgroSell Nova",
                "Hola " + username + ",\n\nTu estado ahora es: " + nuevoEstado + ".");
    }

    @Async("taskExecutor")
    public void sendAcceptedProducerEmail(String to, String username) {
        sendEmail(to, "¡Felicidades! Eres productor",
                "Hola " + username + ",\n\n¡Tu solicitud ha sido aceptada! 🌾 \n\nSaludos,\nEl equipo de AgroSell Nova");
    }

    @Async("taskExecutor")
    public void sendRejectedProducerEmail(String to, String username) {
        sendEmail(to, "Solicitud rechazada",
                "Hola " + username + ",\n\nLamentamos informarte que tu solicitud fue rechazada. \n\nSaludos,\nEl equipo de AgroSell Nova");
    }

    @Async("taskExecutor")
    public void sendProducerApplicationEmail(String to, String username) {
        sendEmail(to, "Solicitud recibida",
                "Hola " + username + ",\n\nHemos recibido tu solicitud para ser productor. \n\nSaludos,\nEl equipo de AgroSell Nova");
    }

    @Async("taskExecutor")
    public void sendNewProductNotification(String to, String productName, Double productPrice) {
        sendEmail(to, "Nuevo producto disponible",
                "Hola,\n\n¡Tenemos un nuevo producto disponible en AgroSell Nova! \n\nProducto: " + productName + "\nPrecio: " + productPrice + "\n\nVisita nuestra plataforma para más detalles y realizar tu compra. \n\nSaludos,\nEl equipo de AgroSell Nova");
    }

    @Async("taskExecutor")
    public void sendNewProductNotificationToAll(List<String> correos, String productName, Double price) {
        for (String correo : correos) {
            if (!correo.contains("@agrosell")) {
                sendNewProductNotification(correo, productName, price);
                System.out.printf("Email de nuevo producto enviado a: %s%n", correo);
            }
        }
    }

    @Async("taskExecutor")
    public void sendBookingConfirmationEmail(String to, String username, String producto, String bookingDate) {
        sendEmail(to, "Confirmación de reserva",
                "Hola " + username + ",\n\nReserva confirmada para: " + producto +
                        "\nFecha: " + bookingDate + ". \n\nSaludos,\nEl equipo de AgroSell Nova");
    }

    @Async("taskExecutor")
    public void sendPaymentConfirmationEmail(String to, String username, String paymentDate, Double amount) {
        sendEmail(to, "Pago confirmado",
                "Hola " + username + ",\n\nPago de $" + amount + " ha sido recibido con éxito el " + paymentDate + ". \n\nGracias por tu compra en AgroSell Nova. \n\nSaludos,\nEl equipo de AgroSell Nova");
    }

    @Async("taskExecutor")
    public void sendResponsePqrsEmail(String to, String username, String respuesta) {
        sendEmail(to, "Respuesta a tu PQRS",
                "Hola " + username + ",\n\n" + respuesta + "\n\nSaludos,\nEl equipo de AgroSell Nova");
    }
}
