package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "detalles_agendas")
public class DetalleAgenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "datos_especifica")
    private String datos_especifica;
    @Column(name = "requiere_participantes")
    private Boolean requiere_participantes;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "fecha_baja")
    private LocalDateTime fecha_baja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agenda_id")
    private Agenda agenda;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtipo_id")
    private SubtipoAgenda subtipo;
}
