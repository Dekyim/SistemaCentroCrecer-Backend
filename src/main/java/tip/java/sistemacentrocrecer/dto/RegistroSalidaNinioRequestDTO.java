package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RegistroSalidaNinioRequestDTO {
    private LocalTime horaSalida;
    private String observaciones;
}