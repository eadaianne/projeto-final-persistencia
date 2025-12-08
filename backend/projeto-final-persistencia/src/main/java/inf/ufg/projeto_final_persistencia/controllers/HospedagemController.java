package inf.ufg.projeto_final_persistencia.controllers;

import inf.ufg.projeto_final_persistencia.dtos.CreateHospedagemDTO;
import inf.ufg.projeto_final_persistencia.dtos.HospedagemDTO;
import inf.ufg.projeto_final_persistencia.services.HospedagemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospedagens")
public class HospedagemController {

    @Autowired
    private HospedagemService service;

    @PostMapping
    public ResponseEntity<HospedagemDTO> create(@Valid @RequestBody CreateHospedagemDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @GetMapping
    public List<HospedagemDTO> listByPonto(@RequestParam Long pontoId) {
        return service.findByPonto(pontoId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
