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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avalRepo;

    @Autowired
    private PontoTuristicoRepository pontoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    /**
     * Upsert: se o usuário já avaliou o ponto, atualiza; caso contrário cria nova avaliação.
     * Retorna a avaliação criada/atualizada como AvaliacaoDTO.
     */
    @Transactional
    public AvaliacaoDTO createOrUpdate(CreateAvaliacaoDTO dto, String username) {
        Usuario u = usuarioRepo.findByLogin(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        PontoTuristico p = pontoRepo.findById(dto.pontoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ponto não encontrado"));

        Integer nota = dto.nota();
        if (nota == null || nota < 1 || nota > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nota inválida (1-5)");
        }

        // verifica se já existe avaliação desse usuário para esse ponto
        return avalRepo.findByPontoIdAndUsuarioId(dto.pontoId(), u.getId())
                .map(existing -> {
                    // update existing
                    existing.setNota(nota);
                    existing.setComentario(dto.comentario());
                    Avaliacao saved = avalRepo.save(existing);
                    // opcional: recalc nota média aqui se seu app não usa trigger DB
                    return toDto(saved);
                })
                .orElseGet(() -> {
                    // create new
                    Avaliacao a = new Avaliacao();
                    a.setPonto(p);
                    a.setUsuario(u);
                    a.setNota(nota);
                    a.setComentario(dto.comentario());
                    Avaliacao saved = avalRepo.save(a);
                    // opcional: recalc nota média aqui se necessário
                    return toDto(saved);
                });
    }

    /**
     * Lista avaliações de um ponto (mais recentes primeiro).
     */
    @Transactional(readOnly = true)
    public List<AvaliacaoDTO> findByPonto(Long pontoId) {
        // use o método do repositório que retorna ordenado por createdAt DESC
        return avalRepo.findByPontoIdOrderByCreatedAtDesc(pontoId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Delete: apenas autor da avaliação ou admin pode deletar.
     */
    @Transactional
    public void delete(Long avaliacaoId, String username) {
        Avaliacao a = avalRepo.findById(avaliacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada"));

        if (!isOwnerOrAdmin(a.getUsuario(), username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir esta avaliação");
        }

        Long pontoId = a.getPonto() != null ? a.getPonto().getId() : null;
        avalRepo.delete(a);
        // opcional: recalc nota média se necessário
    }

    /* ---------- helpers ---------- */

    private boolean isOwnerOrAdmin(Usuario owner, String username) {
        if (owner != null && username.equals(owner.getLogin())) return true;
        return usuarioRepo.findByLogin(username).map(u -> "ADMIN".equalsIgnoreCase(u.getRole())).orElse(false);
    }

    private AvaliacaoDTO toDto(Avaliacao a) {
        return new AvaliacaoDTO(
                a.getId(),
                a.getPonto() != null ? a.getPonto().getId() : null,
                a.getUsuario() != null ? a.getUsuario().getId() : null,
                a.getNota(),
                a.getComentario(),
                a.getCreatedAt()
        );
    }
}
