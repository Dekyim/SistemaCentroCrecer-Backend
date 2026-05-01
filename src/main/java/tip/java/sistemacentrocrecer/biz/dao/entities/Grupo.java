package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "grupos")
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "activo")
    private boolean activo;
    @Column(name = "fecha_baja")
    private LocalDateTime fecha_baja;
    @Column(name = "rango_edad")
    private String rango_edad;
    @Column(name = "hora_inicio")
    private LocalTime hora_inicio;
    @Column(name = "hora_fin")
    private LocalTime hora_fin;

    @OneToMany(mappedBy = "grupo")
    private List<Ninio> ninios;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReporteGrupo> reportes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "grupo_funcionario",
            joinColumns = @JoinColumn(name = "grupo_id"),
            inverseJoinColumns = @JoinColumn(name = "funcionario_id")
    )
    private List<Funcionario> funcionarios;
}
