package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;
import tip.java.sistemacentrocrecer.biz.dao.enums.TipoDiaNoLaborableEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "dias_no_laborables")
public class DiaNoLaborable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "motivo", nullable = false, length = 255)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoDiaNoLaborableEnum tipo;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_id")
    private Funcionario creadoPor;
}
