package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ReporteRequestDTO {
    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 3, max = 200, message = "El título debe tener entre 3 y 200 caracteres")
    private String titulo;

    @Size(max = 3000, message = "La descripción no puede superar los 3000 caracteres")
    private String descripcion;

    @NotNull(message = "El id del funcionario no puede ser nulo")
    private Integer funcionarioId;

    //IDs de grupos a asociar al reporte
    private List<Integer> gruposIds;

    //IDs de niños a asociar al reporte
    private List<Integer> niniosIds;

    //Documentos adjuntos al momento de crear el reporte
    private List<DocumentoAdjuntoRequestDTO> documentos;

}
