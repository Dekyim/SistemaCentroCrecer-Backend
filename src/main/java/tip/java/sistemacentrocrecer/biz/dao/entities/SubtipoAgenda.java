package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

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
    private Integer subtipoId;
    @Column(name = "subtipo", nullable = false)
    private String subtipo;

    @OneToOne(mappedBy = "subtipo", fetch = FetchType.LAZY)
    private DetalleAgenda detalleAgenda;

    @OneToOne(mappedBy = "subtipoAgenda", fetch = FetchType.LAZY)
    private AgendaLimpieza agendaLimpieza;

}
