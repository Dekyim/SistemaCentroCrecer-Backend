package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "subtipos_agendas")
public class SubtipoAgenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtipo_id")
    private Integer subtipoId;

    @Column(name = "subtipo", nullable = false)
    private String subtipo;

    @OneToMany(mappedBy = "subtipoAgenda", fetch = FetchType.LAZY)
    private List<DetalleAgenda> detallesAgenda;

    @OneToMany(mappedBy = "subtipoAgenda", fetch = FetchType.LAZY)
    private List<AgendaLimpieza> agendasLimpieza;

}
