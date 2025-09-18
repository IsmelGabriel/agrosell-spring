package com.agrosellnova.Agrosellnova.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Bienvenido a AgrosellNova");
        message.setText("Hola " + username + ",\n\n¡Gracias por registrarte en AgroSell Nova! 🌱\n\n" +
                "Ahora puedes acceder a todos nuestros servicios.\n\n" +
                "Saludos,\nEl equipo de AgrosellNova");

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
                "Tu rol en AgrosellNova ha sido actualizado a: " + nuevoRol + ".\n\n" +
                "Si tienes dudas, por favor comunícate con soporte.");
        mailSender.send(message);
    }
}
