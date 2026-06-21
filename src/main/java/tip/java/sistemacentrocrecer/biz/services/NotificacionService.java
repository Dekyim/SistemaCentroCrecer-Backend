package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Notificacion;
import tip.java.sistemacentrocrecer.biz.dao.entities.Reporte;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NotificacionRepository;
import tip.java.sistemacentrocrecer.dto.NotificacionResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    @Transactional
    public void crearNotificacion(Funcionario funcionario, Reporte reporte,
                                  String nombreResponsable, String nombreNinio) {
        if (notificacionRepository.existsByFuncionario_IdAndReporte_Id(
                funcionario.getId(), reporte.getId())) {
            return;
        }

        String mensaje = "El responsable " + nombreResponsable +
                " vio el reporte: " + reporte.getTitulo();

        if (nombreNinio != null && !nombreNinio.isBlank()) {
            mensaje += " (" + nombreNinio + ")";
        }

        Notificacion notificacion = Notificacion.builder()
                .funcionario(funcionario)
                .reporte(reporte)
                .mensaje(mensaje)
                .leida(false)
                .fechaCreacion(LocalDateTime.now())
                .build();

        notificacionRepository.save(notificacion);
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> listarPorFuncionario(Integer funcionarioId) {
        return notificacionRepository
                .findByFuncionario_IdOrderByFechaCreacionDesc(funcionarioId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Long contarNoLeidas(Integer funcionarioId) {
        return notificacionRepository.countByFuncionario_IdAndLeidaFalse(funcionarioId);
    }

    @Transactional
    public void marcarComoLeida(Integer notificacionId) {
        notificacionRepository.findById(notificacionId).ifPresent(notificacion -> {
            notificacion.setLeida(true);
            notificacionRepository.save(notificacion);
        });
    }

    @Transactional
    public void marcarTodasComoLeidas(Integer funcionarioId) {
        notificacionRepository.marcarTodasComoLeidas(funcionarioId);
    }

    private NotificacionResponseDTO toDTO(Notificacion notificacion) {
        NotificacionResponseDTO dto = new NotificacionResponseDTO();
        dto.setId(notificacion.getId());
        dto.setMensaje(notificacion.getMensaje());
        dto.setLeida(notificacion.getLeida());
        dto.setFechaCreacion(notificacion.getFechaCreacion());

        if (notificacion.getReporte() != null) {
            dto.setReporteId(notificacion.getReporte().getId());
            dto.setReporteTitulo(notificacion.getReporte().getTitulo());
        }

        return dto;
    }
}
