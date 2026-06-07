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
    public void crearNotificacion(Funcionario funcionario, Reporte reporte, String nombreResponsable) {
        Notificacion notif = Notificacion.builder()
                .funcionario(funcionario)
                .reporte(reporte)
                .mensaje("El responsable " + nombreResponsable + " leyó tu reporte \"" + reporte.getTitulo() + "\"")
                .leida(false)
                .fechaCreacion(LocalDateTime.now())
                .build();
        notificacionRepository.save(notif);
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
        notificacionRepository.findById(notificacionId).ifPresent(n -> {
            n.setLeida(true);
            notificacionRepository.save(n);
        });
    }

    @Transactional
    public void marcarTodasComoLeidas(Integer funcionarioId) {
        notificacionRepository.marcarTodasComoLeidas(funcionarioId);
    }

    private NotificacionResponseDTO toDTO(Notificacion n) {
        NotificacionResponseDTO dto = new NotificacionResponseDTO();
        dto.setId(n.getId());
        dto.setMensaje(n.getMensaje());
        dto.setLeida(n.getLeida());
        dto.setFechaCreacion(n.getFechaCreacion());
        if (n.getReporte() != null) {
            dto.setReporteId(n.getReporte().getId());
            dto.setReporteTitulo(n.getReporte().getTitulo());
        }
        return dto;
    }
}