package tip.java.sistemacentrocrecer.biz.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tip.java.sistemacentrocrecer.biz.dao.entities.DiaNoLaborable;
import tip.java.sistemacentrocrecer.biz.dao.repositories.DiaNoLaborableRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarioLaboralServiceTests {

    @Mock
    private DiaNoLaborableRepository diaNoLaborableRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private CalendarioLaboralService calendarioLaboralService;

    @Test
    void cuentaLunesAViernesExcluyendoDiasNoLaborables() {
        LocalDate desde = LocalDate.of(2026, 6, 15);
        LocalDate hasta = LocalDate.of(2026, 6, 21);
        LocalDate feriado = LocalDate.of(2026, 6, 17);

        when(diaNoLaborableRepository.findByFechaBetweenAndActivoTrueOrderByFechaAsc(desde, hasta))
                .thenReturn(List.of(
                        DiaNoLaborable.builder().fecha(feriado).activo(true).build()
                ));

        long total = calendarioLaboralService.contarDiasHabiles(desde, hasta);

        assertEquals(4, total);
    }

    @Test
    void reconoceUnaFechaActivaComoNoLaborable() {
        LocalDate fecha = LocalDate.of(2026, 6, 19);
        when(diaNoLaborableRepository.existsByFechaAndActivoTrue(fecha)).thenReturn(true);

        assertTrue(calendarioLaboralService.esDiaNoLaborable(fecha));
    }
}
