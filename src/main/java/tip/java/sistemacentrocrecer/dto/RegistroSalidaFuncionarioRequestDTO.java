package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RegistroSalidaFuncionarioRequestDTO {
    private LocalDate fecha;
    private LocalTime horaSalida;
    private String observaciones;
}