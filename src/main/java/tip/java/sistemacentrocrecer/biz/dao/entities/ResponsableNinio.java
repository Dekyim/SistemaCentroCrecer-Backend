package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "responsables_ninios")
public class ResponsableNinio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "tipo_relacion")
    private String tipoRelacion;
    @Column(name = "autorizado_retiro")
    private Boolean autorizadoRetiro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ninio_id")
    private Ninio ninio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Responsable responsable;
}
