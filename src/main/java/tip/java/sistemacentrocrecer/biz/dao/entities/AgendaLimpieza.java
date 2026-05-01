package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoLimpiezaEnum;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "agendas_limpiezas")
public class AgendaLimpieza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "zona")
    private String zona;
    @Column(name = "frecuencia")
    private Integer frecuencia;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoLimpiezaEnum estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agenda_id")
    private Agenda agenda;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtipo_agenda_id")
    private SubtipoAgenda subtipoAgenda;
}
