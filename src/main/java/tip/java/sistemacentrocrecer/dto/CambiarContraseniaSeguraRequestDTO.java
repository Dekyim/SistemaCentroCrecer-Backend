package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

@Data
public class CambiarContraseniaSeguraRequestDTO {
    private String contraseniaActual;
    private String nuevaContrasenia;
}