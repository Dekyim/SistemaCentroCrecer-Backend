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
@Table(name = "empresas_externas")
public class EmpresaExterna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "tipo_servicio")
    private String tipo_servicio;
    @Column(name = "telefono")
    private String telefono;

    @OneToMany(mappedBy = "empresaExterna", fetch = FetchType.LAZY)
    private List<Actividad> actividades;
}
