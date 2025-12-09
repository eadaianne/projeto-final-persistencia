package inf.ufg.projeto_final_persistencia.controllers;

import inf.ufg.projeto_final_persistencia.dtos.CreatePontoDTO;
import inf.ufg.projeto_final_persistencia.dtos.PontoDTO;
import inf.ufg.projeto_final_persistencia.dtos.UpdatePontoDTO;
import inf.ufg.projeto_final_persistencia.services.PontoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/pontos")
public class PontoController {

    @Autowired
    private PontoService service;

    @GetMapping
    public Page<PontoDTO> list(
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) BigDecimal notaMin,
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.list(cidade, notaMin, nome, page, size);
    }

    @GetMapping("/{id: \\d+}")
    public ResponseEntity<PontoDTO> get(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PontoDTO> create(
            @Valid @RequestBody CreatePontoDTO dto,
            Authentication auth) {
        String username = auth.getName(); // vem do JWT automaticamente
        PontoDTO created = service.create(dto, username);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePontoDTO dto,
            Authentication auth) {
        String username = auth.getName();
        PontoDTO updated = service.update(id, dto, username);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication auth) {
        String username = auth.getName();
        service.delete(id, username);
        return ResponseEntity.noContent().build();
    }
}
