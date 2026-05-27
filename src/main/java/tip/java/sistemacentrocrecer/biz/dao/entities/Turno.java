package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "turnos")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "turno_dias", joinColumns = @JoinColumn(name = "turno_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "dia")
    @Builder.Default
    private List<DayOfWeek> dias = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;
}