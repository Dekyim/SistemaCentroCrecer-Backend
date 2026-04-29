package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="condiciones_medicas")
public class CondicionMedica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer condicionId;
    @Column(name = "condicion")
    private String condicion;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "es_cronica")
    private Boolean es_cronica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ninio_id")
    private Ninio ninio;
}
