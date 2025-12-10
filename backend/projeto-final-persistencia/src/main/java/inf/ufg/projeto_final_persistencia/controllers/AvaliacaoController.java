package inf.ufg.projeto_final_persistencia.controllers;

import inf.ufg.projeto_final_persistencia.dtos.AvaliacaoDTO;
import inf.ufg.projeto_final_persistencia.dtos.CreateAvaliacaoDTO;
import inf.ufg.projeto_final_persistencia.services.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService service;

    // Cria ou atualiza a avaliação usando o usuário autenticado (do token)
    @PostMapping
    public ResponseEntity<AvaliacaoDTO> createOrUpdate(
            @Valid @RequestBody CreateAvaliacaoDTO dto,
            Authentication auth) {

        String username = auth.getName(); // login do usuário no token
        AvaliacaoDTO saved = service.createOrUpdate(dto, username);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/ponto/{pontoId}")
    public ResponseEntity<List<AvaliacaoDTO>> listByPonto(@PathVariable Long pontoId) {
        return ResponseEntity.ok(service.findByPonto(pontoId));
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
