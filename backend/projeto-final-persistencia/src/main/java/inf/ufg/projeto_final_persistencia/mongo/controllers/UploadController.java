package inf.ufg.projeto_final_persistencia.mongo.controllers;

import inf.ufg.projeto_final_persistencia.mongo.dtos.FotoDTO;
import inf.ufg.projeto_final_persistencia.mongo.services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/fotos")
public class UploadController {

    @Autowired
    private UploadService service;

    @PostMapping("/ponto/{pontoId}")
    public ResponseEntity<FotoDTO> upload(
            @PathVariable Long pontoId,
            @RequestParam("file") MultipartFile file,
            Authentication auth
    ) {
        FotoDTO saved = service.uploadFile(pontoId, file, auth.getName());
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping("/ponto/{pontoId}")
    public ResponseEntity<List<FotoDTO>> list(@PathVariable Long pontoId) {
        return ResponseEntity.ok(service.listByPonto(pontoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication auth) {
        service.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
