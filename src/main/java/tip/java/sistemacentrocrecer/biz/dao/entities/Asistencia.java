package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "asistencias")
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "fecha")
    private LocalDate fecha;
    @Column(name = "hora_entrada")
    private LocalTime horaEntrada;
    @Column(name = "hora_salida")
    private LocalTime horaSalida;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "ninio_cedula")
    private String ninio_cedula;
    @Column(name = "ninio_nombre")
    private String ninio_nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ninio_id")
    private Ninio ninio;

    @Column(name = "funcionario_nombre")
    private String funcionario_nombre;
    @Column(name = "funcionario_cedula")
    private String funcionario_cedula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;
}
