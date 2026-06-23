package tip.java.sistemacentrocrecer.biz.services;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
                                               String tituloReporte, String descripcionReporte,
                                               String fechaReporte, String nombreNinio) {
        if (mailSender == null || fromEmail.isBlank()) {
            log.warn("Email no configurado - notificacion no enviada a {}", destinatario);
            return;
        }
        try {
            enviarHtml(
                    destinatario,
                    tituloReporte,
                    construirHtmlReporte(nombreResponsable, tituloReporte, descripcionReporte, fechaReporte, nombreNinio)
            );
            log.info("Email enviado a {} por nuevo reporte '{}'", destinatario, tituloReporte);
        } catch (Exception e) {
            log.error("Error al enviar email a {}: {}", destinatario, e.getMessage());
        }
    }

    @Async
    public void enviarNotificacionNuevaActividad(String destinatario, String nombreResponsable,
                                                 String nombreActividad, String descripcion,
                                                 String fechaDesde, String fechaHasta,
                                                 String horaInicio, String horaSalida,
                                                 String lugar, String nombreNinio) {
        if (mailSender == null || fromEmail.isBlank()) {
            log.warn("Email no configurado - notificacion no enviada a {}", destinatario);
            return;
        }
        try {
            enviarHtml(
                    destinatario,
                    nombreActividad,
                    construirHtmlActividad(
                            nombreResponsable,
                            nombreActividad,
                            descripcion,
                            fechaDesde,
                            fechaHasta,
                            horaInicio,
                            horaSalida,
                            lugar,
                            nombreNinio
                    )
            );
            log.info("Email enviado a {} por nueva actividad '{}'", destinatario, nombreActividad);
        } catch (Exception e) {
            log.error("Error al enviar email a {}: {}", destinatario, e.getMessage());
        }
    }

    private void enviarHtml(String destinatario, String asunto, String html) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(html, true);
        mailSender.send(message);
    }

    private String construirHtmlReporte(String nombreResponsable, String tituloReporte,
                                        String descripcionReporte, String fechaReporte,
                                        String nombreNinio) {
        return plantillaHtml(
                tituloReporte,
                "Hola " + escaparHtml(nombreResponsable) + ", se genero un nuevo reporte.",
                fila("Niño/a", destacarNinios(nombreNinio)) +
                        fila("Reporte", escaparHtml(tituloReporte)) +
                        filaSiExiste("Fecha", fechaReporte) +
                        filaSiExiste("Descripcion", descripcionReporte)
        );
    }

    private String construirHtmlActividad(String nombreResponsable, String nombreActividad, String descripcion,
                                          String fechaDesde, String fechaHasta, String horaInicio,
                                          String horaSalida, String lugar, String nombreNinio) {
        String filas = fila("Niño/a", destacarNinios(nombreNinio)) +
                fila("Fecha inicio", escaparHtml(fechaDesde)) +
                filaSiExiste("Fecha fin", fechaHasta) +
                fila("Hora inicio", escaparHtml(horaInicio)) +
                filaSiExiste("Hora salida", horaSalida) +
                filaSiExiste("Lugar", lugar) +
                filaSiExiste("Descripcion", descripcion);

        return plantillaHtml(
                nombreActividad,
                "Hola " + escaparHtml(nombreResponsable) + ", se creo una nueva actividad.",
                filas
        );
    }

    private String plantillaHtml(String titulo, String introduccion, String filas) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="margin:0;padding:0;background:#f4f6f8;font-family:Arial,Helvetica,sans-serif;color:#1f2937;">
                  <div style="max-width:640px;margin:0 auto;padding:24px;">
                    <div style="background:#ffffff;border:1px solid #e5e7eb;border-radius:8px;padding:24px;">
                      <h1 style="margin:0 0 16px;font-size:24px;line-height:1.3;color:#0f172a;">%s</h1>
                      <p style="margin:0 0 20px;font-size:15px;line-height:1.5;">%s</p>
                      <table style="width:100%%;border-collapse:collapse;font-size:14px;">
                        %s
                      </table>
                      <p style="margin:24px 0 0;font-size:13px;color:#64748b;">Centro Crecer - Sistema de Gestion</p>
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(escaparHtml(titulo), introduccion, filas);
    }

    private String fila(String etiqueta, String valor) {
        return """
                <tr>
                  <td style="padding:10px 12px;border-top:1px solid #e5e7eb;font-weight:bold;width:150px;color:#334155;">%s</td>
                  <td style="padding:10px 12px;border-top:1px solid #e5e7eb;color:#111827;">%s</td>
                </tr>
                """.formatted(escaparHtml(etiqueta), valor);
    }

    private String filaSiExiste(String etiqueta, String valor) {
        if (valor == null || valor.isBlank()) {
            return "";
        }
        return fila(etiqueta, escaparHtml(valor));
    }

    private String destacarNinios(String nombres) {
        if (nombres == null || nombres.isBlank()) {
            return "<strong>-</strong>";
        }
        String[] partes = nombres.split(",");
        if (partes.length == 1) {
            return "<strong>" + escaparHtml(nombres.trim()) + "</strong>";
        }

        StringBuilder html = new StringBuilder("<ul style=\"margin:0;padding-left:18px;\">");
        for (String parte : partes) {
            String nombre = parte.trim();
            if (!nombre.isBlank()) {
                html.append("<li><strong>").append(escaparHtml(nombre)).append("</strong></li>");
            }
        }
        html.append("</ul>");
        return html.toString();
    }

    private String escaparHtml(String valor) {
        if (valor == null) {
            return "";
        }
        return valor
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
