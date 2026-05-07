package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DocumentoAdjuntoRequestDTO {
    @NotBlank(message = "El nombre de archivo no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombreArchivo;
    @NotBlank(message = "El tipo de archivo no puede estar vacío")
    @Size(min = 2, max = 50, message = "El tipo de archivo debe tener entre 2 y 50 caracteres")
    private String tipoArchivo;
    @NotBlank(message = "El URL no puede estar vacío")
    @Size(min = 10, max = 255, message = "El URL debe tener entre 10 y 255 caracteres")
    private String url;
    @NotNull(message = "El reporte es obligatorio")
    private Integer reporteId;

}
