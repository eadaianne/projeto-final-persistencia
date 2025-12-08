package inf.ufg.projeto_final_persistencia.services;

import inf.ufg.projeto_final_persistencia.dtos.AvaliacaoDTO;
import inf.ufg.projeto_final_persistencia.dtos.CreateAvaliacaoDTO;
import inf.ufg.projeto_final_persistencia.entities.Avaliacao;
import inf.ufg.projeto_final_persistencia.entities.PontoTuristico;
import inf.ufg.projeto_final_persistencia.entities.Usuario;
import inf.ufg.projeto_final_persistencia.repositories.AvaliacaoRepository;
import inf.ufg.projeto_final_persistencia.repositories.PontoTuristicoRepository;
import inf.ufg.projeto_final_persistencia.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepo;
    @Autowired
    private PontoTuristicoRepository pontoRepo;
    @Autowired
    private UsuarioRepository usuarioRepo;

    @Transactional
    public AvaliacaoDTO createOrUpdate(CreateAvaliacaoDTO dto) {
        PontoTuristico ponto = pontoRepo.findById(dto.pontoId()).orElseThrow(() -> new IllegalArgumentException("Ponto não encontrado"));
        Usuario usuario = usuarioRepo.findById(dto.usuarioId()).orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));

        return avaliacaoRepo.findByPontoAndUsuario(ponto, usuario)
                .map(existing -> {
                    existing.setNota(dto.nota());
                    existing.setComentario(dto.comentario());
                    Avaliacao updated = avaliacaoRepo.save(existing);
                    // trigger DB recalcula nota_media automaticamente
                    return toDto(updated);
                })
                .orElseGet(() -> {
                    Avaliacao a = Avaliacao.builder()
                            .ponto(ponto)
                            .usuario(usuario)
                            .nota(dto.nota())
                            .comentario(dto.comentario())
                            .createdAt(Instant.now())
                            .build();
                    Avaliacao saved = avaliacaoRepo.save(a);
                    return toDto(saved);
                });
    }

    public List<AvaliacaoDTO> findByPonto(Long pontoId) {
        PontoTuristico ponto = pontoRepo.findById(pontoId).orElseThrow(() -> new IllegalArgumentException("Ponto não encontrado"));
        return avaliacaoRepo.findByPontoOrderByCreatedAtDesc(ponto).stream().map(this::toDto).collect(Collectors.toList());
    }

    private AvaliacaoDTO toDto(Avaliacao a) {
        return new AvaliacaoDTO(a.getId(), a.getPonto().getId(), a.getUsuario().getId(), a.getNota(), a.getComentario(), a.getCreatedAt());
    }
}
