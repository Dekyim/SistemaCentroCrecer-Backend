package tip.java.sistemacentrocrecer.biz.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Async
    public void enviarNotificacionNuevoReporte(String destinatario, String nombreResponsable,
                                               String tituloReporte, String nombreNinio) {
        if (mailSender == null || fromEmail.isBlank()) {
            log.warn("Email no configurado - notificacion no enviada a {}", destinatario);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(destinatario);
            message.setSubject("Nuevo reporte disponible - Centro Crecer");
            message.setText(
                    "Hola " + nombreResponsable + ",\n\n" +
                            "Se genero un nuevo reporte para " + nombreNinio + ": \"" + tituloReporte + "\".\n\n" +
                            "Podes ingresar al sistema para ver mas detalles.\n\n" +
                            "Centro Crecer - Sistema de Gestion"
            );
            mailSender.send(message);
            log.info("Email enviado a {} por nuevo reporte '{}'", destinatario, tituloReporte);
        } catch (Exception e) {
            log.error("Error al enviar email a {}: {}", destinatario, e.getMessage());
        }
    }
}
