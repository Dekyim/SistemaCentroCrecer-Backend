package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "responsables")
public class Responsable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "cedula", unique = true, nullable = false , length = 8)
    private String cedula;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "apellido", nullable = false)
    private String apellido;
    @Column(name = "fecha_nacimiento")
    private Date fecha_nacimiento;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "fecha_baja")
    private LocalDateTime fecha_baja;
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResponsableNinio> ninios;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "responsable_inscripcion",
            joinColumns = @JoinColumn(name = "responsable_id"),
            inverseJoinColumns = @JoinColumn(name = "inscripcion_id")
    )
    private List<Inscripcion> inscripciones;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "responsable_permiso",
            joinColumns = @JoinColumn(name = "responsable_id"),
            inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    private List<Permiso> permisos;
}
