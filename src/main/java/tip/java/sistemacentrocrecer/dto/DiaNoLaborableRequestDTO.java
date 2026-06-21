package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tip.java.sistemacentrocrecer.biz.dao.enums.TipoDiaNoLaborableEnum;

import java.time.LocalDate;

@Data
public class DiaNoLaborableRequestDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 255, message = "El motivo no puede superar los 255 caracteres")
    private String motivo;

    @NotNull(message = "El tipo es obligatorio")
    private TipoDiaNoLaborableEnum tipo;
}
