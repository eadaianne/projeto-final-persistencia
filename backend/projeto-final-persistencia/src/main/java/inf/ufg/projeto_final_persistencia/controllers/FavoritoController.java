package inf.ufg.projeto_final_persistencia.controllers;

import inf.ufg.projeto_final_persistencia.dtos.FavoritoDTO;
import inf.ufg.projeto_final_persistencia.services.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService service;

    @PostMapping("/{pontoId}")
    public ResponseEntity<FavoritoDTO> marcar(
            @PathVariable Long pontoId,
            Authentication auth
    ) {
        FavoritoDTO dto = service.marcarFavorito(pontoId, auth.getName());
        return ResponseEntity.status(201).body(dto);
    }

    @DeleteMapping("/{pontoId}")
    public ResponseEntity<Void> remover(
            @PathVariable Long pontoId,
            Authentication auth
    ) {
        service.removerFavorito(pontoId, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<FavoritoDTO>> meusFavoritos(Authentication auth) {
        return ResponseEntity.ok(service.listarMeusFavoritos(auth.getName()));
    }
}
