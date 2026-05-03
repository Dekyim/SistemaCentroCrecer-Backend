package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;
import tip.java.sistemacentrocrecer.biz.dao.enums.SexoNinioEnum;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "ninios")
public class Ninio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "cedula", unique = true, nullable = false , length = 8)
    private String cedula;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private SexoNinioEnum sexo;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;
    @Column(name = "activo")
    private boolean activo;
    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    @OneToMany(mappedBy = "ninio", cascade = CascadeType.ALL)
    private List<CondicionMedica> condiciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

    @OneToMany(mappedBy = "ninio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReporteNinio> reportes;

    @OneToMany(mappedBy = "ninio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asistencia> asistencias;

    @OneToMany(mappedBy = "ninio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResponsableNinio> responsables;

    @OneToMany(mappedBy = "ninio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Permiso> permisos;

    @OneToMany(mappedBy = "ninio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones;
}
