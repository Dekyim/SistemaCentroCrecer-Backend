package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(
        name = "permisos",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_permiso_ninio_actividad",
                        columnNames = {"ninio_cedula", "actividad_id"}
                )
        }
)
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;
    @Column(name = "autorizado")
    private Boolean autorizado;

    @Column(name = "respondido")
    private Boolean respondido = false;

    @Column(name = "ninio_cedula", nullable = false , length = 8)
    private String ninioCedula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id")
    private Actividad actividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ninio_id")
    private Ninio ninio;

    @ManyToMany(mappedBy = "permisos", fetch = FetchType.LAZY)
    private List<Responsable> responsables;

}
