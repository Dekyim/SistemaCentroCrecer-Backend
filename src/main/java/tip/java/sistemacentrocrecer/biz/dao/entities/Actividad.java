package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "actividades")
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "fecha_desde")
    private LocalDate fechaDesde;
    @Column(name = "fecha_hasta")
    private LocalDate fechaHasta;
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;
    @Column(name = "hora_salida")
    private LocalTime horaSalida;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;
    @Column(name = "lugar")
    private String lugar;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "actividad_ninio",
            joinColumns = @JoinColumn(name = "actividad_id"),
            inverseJoinColumns = @JoinColumn(name = "ninio_id")
    )
    private List<Ninio> ninios;

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Permiso> permisos;

    @OneToMany(mappedBy = "actividad", fetch = FetchType.LAZY)
    private List<EmpresaExterna> empresasExternas;
}
