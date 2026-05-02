package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "reportes_ninios")
public class ReporteNinio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "reporte_titulo")
    private String reporte_titulo;
    @Column(name = "nombre_ninio")
    private String nombre_ninio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ninio_id")
    private Ninio ninio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporte_id")
    private Reporte reporte;
}
