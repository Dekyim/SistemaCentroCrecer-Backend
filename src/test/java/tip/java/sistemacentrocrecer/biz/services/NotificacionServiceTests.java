package tip.java.sistemacentrocrecer.biz.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Notificacion;
import tip.java.sistemacentrocrecer.biz.dao.entities.Reporte;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NotificacionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTests {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    @Test
    void creaNotificacionAsociadaAlFuncionarioYReporte() {
        Funcionario funcionario = Funcionario.builder().id(7).build();
        Reporte reporte = Reporte.builder().id(12).titulo("Informe mensual").build();

        when(notificacionRepository.existsByFuncionario_IdAndReporte_Id(7, 12))
                .thenReturn(false);

        notificacionService.crearNotificacion(
                funcionario,
                reporte,
                "Ana Perez",
                "Juan Gomez"
        );

        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionRepository).save(captor.capture());

        Notificacion notificacion = captor.getValue();
        assertSame(funcionario, notificacion.getFuncionario());
        assertSame(reporte, notificacion.getReporte());
        assertEquals(
                "El responsable Ana Perez vio el reporte: Informe mensual (Juan Gomez)",
                notificacion.getMensaje()
        );
        assertFalse(notificacion.getLeida());
    }

    @Test
    void noDuplicaNotificacionParaElMismoFuncionarioYReporte() {
        Funcionario funcionario = Funcionario.builder().id(7).build();
        Reporte reporte = Reporte.builder().id(12).titulo("Informe mensual").build();

        when(notificacionRepository.existsByFuncionario_IdAndReporte_Id(7, 12))
                .thenReturn(true);

        notificacionService.crearNotificacion(
                funcionario,
                reporte,
                "Ana Perez",
                null
        );

        verify(notificacionRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
