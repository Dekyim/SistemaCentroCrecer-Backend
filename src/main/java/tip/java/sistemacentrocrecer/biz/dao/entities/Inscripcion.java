package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoInscripcionEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "inscripciones")
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "fecha_inscripcion")
    private LocalDate fecha_inscripcion;
    @Column(name = "fecha_inicio")
    private LocalDate fecha_inicio;
    @Column(name = "fecha_fin")
    private LocalDate fecha_fin;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_inscripcion")
    private EstadoInscripcionEnum estadoInscripcion;
    @Column(name = "fecha_baja")
    private LocalDateTime fecha_baja;
    @Column(name = "motivo_baja")
    private String motivo_baja;
    @Column(name = "observaciones")
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ninio_id")
    private Ninio ninio;

    @ManyToMany(mappedBy = "inscripciones", fetch = FetchType.LAZY)
    private List<Responsable> responsables;
}
