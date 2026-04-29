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
@Table(name = "tipo_agendas")
public class TipoAgenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @OneToOne(mappedBy = "tipo", fetch = FetchType.LAZY)
    private Agenda agenda;
}
