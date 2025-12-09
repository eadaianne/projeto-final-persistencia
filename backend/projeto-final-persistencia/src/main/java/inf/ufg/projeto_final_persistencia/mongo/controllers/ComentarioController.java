package inf.ufg.projeto_final_persistencia.mongo.controllers;

import inf.ufg.projeto_final_persistencia.mongo.services.ComentarioService;
import inf.ufg.projeto_final_persistencia.mongo.dtos.CreateComentarioDTO;
import inf.ufg.projeto_final_persistencia.mongo.dtos.ComentarioDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService service;

    @PostMapping
    public ResponseEntity<ComentarioDTO> create(
            @Valid @RequestBody CreateComentarioDTO dto,
            Authentication auth
    ) {
        ComentarioDTO saved = service.create(dto, auth.getName());
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping("/ponto/{pontoId}")
    public ResponseEntity<List<ComentarioDTO>> list(@PathVariable Long pontoId) {
        return ResponseEntity.ok(service.listByPonto(pontoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication auth) {
        service.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
