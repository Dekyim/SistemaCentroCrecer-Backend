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
    public void enviarNotificacionVisto(String destinatario, String nombreFuncionario,
                                        String tituloReporte, String nombreResponsable) {
        if (mailSender == null || fromEmail.isBlank()) {
            log.warn("Email no configurado — notificación no enviada a {}", destinatario);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(destinatario);
            message.setSubject("Tu reporte fue leído — Centro Crecer");
            message.setText(
                    "Hola " + nombreFuncionario + ",\n\n" +
                            "El responsable " + nombreResponsable + " ha leído el reporte \"" + tituloReporte + "\".\n\n" +
                            "Podés ingresar al sistema para ver más detalles.\n\n" +
                            "Centro Crecer — Sistema de Gestión"
            );
            mailSender.send(message);
            log.info("Email enviado a {} sobre reporte '{}'", destinatario, tituloReporte);
        } catch (Exception e) {
            log.error("Error al enviar email a {}: {}", destinatario, e.getMessage());
        }
    }
}