package tip.java.sistemacentrocrecer.biz.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "documentos_adjuntos")
public class DocumentoAdjunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Column(name = "tipo_archivo")
    private String tipoArchivo;
    @Column(name = "url")
    private String url;
    @Column(name = "fecha_subida")
    private Date fechaSubida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporte_id")
    private Reporte reporte;
}
