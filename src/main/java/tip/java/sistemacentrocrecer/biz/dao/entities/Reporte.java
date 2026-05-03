package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "reportes")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "fecha_generacion")
    private Date fechaGeneracion;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "visto")
    private Boolean visto;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    @OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReporteNinio> reporteNinios;

    @OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReporteGrupo> reporteGrupos;

    @OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DocumentoAdjunto> documentos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;


}
