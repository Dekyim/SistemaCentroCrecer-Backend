package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.NinioService;
import tip.java.sistemacentrocrecer.dto.NinioFotoRequestDTO;
import tip.java.sistemacentrocrecer.dto.NinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.NinioResponsableUpdateDTO;
import tip.java.sistemacentrocrecer.dto.NinioResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ninios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NinioController {
    private final NinioService ninioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NinioResponseDTO crear(@Valid @RequestBody NinioRequestDTO dto) {
        return ninioService.crear(dto);
    }

    @GetMapping
    public List<NinioResponseDTO> listar() {
        return ninioService.listar();
    }

    @GetMapping("/{id}")
    public NinioResponseDTO obtenerPorId(@PathVariable Integer id) {
        return ninioService.obtenerPorId(id);
    }

    @PutMapping("/{id}/actualizar")
    public NinioResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody NinioRequestDTO dto) {
        return ninioService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}/baja")
    public void eliminar(@PathVariable Integer id) {
        ninioService.eliminar(id);
    }

    @PutMapping("/{id}/foto")
    public NinioResponseDTO actualizarFoto(@PathVariable Integer id,
                                           @RequestBody NinioFotoRequestDTO dto) {
        return ninioService.actualizarFoto(id, dto.getFotoUrl());
    }

    @GetMapping("/mis-ninios")
    @PreAuthorize("hasRole('RESPONSABLE')")
    public List<NinioResponseDTO> misNinios(@RequestParam Integer responsableId) {
        return ninioService.listarPorResponsable(responsableId);
    }

    @PutMapping("/{id}/responsable-actualizar")
    @PreAuthorize("hasRole('RESPONSABLE')")
    public NinioResponseDTO actualizarPorResponsable(@PathVariable Integer id, @RequestParam Integer responsableId, @Valid @RequestBody NinioResponsableUpdateDTO dto) {
        return ninioService.actualizarPorResponsable(id, responsableId, dto);
    }
}