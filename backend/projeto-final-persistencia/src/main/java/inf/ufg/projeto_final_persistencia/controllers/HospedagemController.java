package inf.ufg.projeto_final_persistencia.controllers;

import inf.ufg.projeto_final_persistencia.dtos.CreateHospedagemDTO;
import inf.ufg.projeto_final_persistencia.dtos.HospedagemDTO;
import inf.ufg.projeto_final_persistencia.services.HospedagemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospedagens")
public class HospedagemController {

    @Autowired
    private HospedagemService service;

    @PostMapping
    public ResponseEntity<HospedagemDTO> create(
            @Valid @RequestBody CreateHospedagemDTO dto,
            Authentication auth) {
        String username = auth == null ? null : auth.getName();
        HospedagemDTO created = service.create(dto, username);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<HospedagemDTO> listByPonto(@RequestParam Long pontoId) {
        return service.findByPonto(pontoId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication auth) {
        String username = auth == null ? null : auth.getName();
        service.delete(id, username);
        return ResponseEntity.noContent().build();
    }
}
