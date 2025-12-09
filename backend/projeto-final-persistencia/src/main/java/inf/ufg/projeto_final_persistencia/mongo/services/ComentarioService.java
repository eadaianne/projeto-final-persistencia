package inf.ufg.projeto_final_persistencia.mongo.services;

import inf.ufg.projeto_final_persistencia.entities.Usuario;
import inf.ufg.projeto_final_persistencia.mongo.entities.Comentario;
import inf.ufg.projeto_final_persistencia.mongo.repositories.ComentarioRepository;
import inf.ufg.projeto_final_persistencia.mongo.dtos.ComentarioDTO;
import inf.ufg.projeto_final_persistencia.mongo.dtos.CreateComentarioDTO;
import inf.ufg.projeto_final_persistencia.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository repo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    public ComentarioDTO toDto(Comentario c) {
        ComentarioDTO dto = new ComentarioDTO();
        dto.setId(c.getId());
        dto.setUsuarioId(c.getUsuarioId());
        dto.setPontoId(c.getPontoId());
        dto.setTexto(c.getTexto());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }

    public ComentarioDTO create(CreateComentarioDTO dto, String username) {
        Usuario u = usuarioRepo.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Comentario c = new Comentario();
        c.setUsuarioId(u.getId());
        c.setPontoId(dto.getPontoId());
        c.setTexto(dto.getTexto());
        c.setDevice(dto.getDevice());
        c.setLanguage(dto.getLanguage());

        repo.save(c);
        return toDto(c);
    }

    public List<ComentarioDTO> listByPonto(Long pontoId) {
        return repo.findByPontoIdOrderByCreatedAtDesc(pontoId)
                   .stream()
                   .map(this::toDto)
                   .toList();
    }

    public void delete(String comentarioId, String username) {
        Comentario c = repo.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

        Usuario u = usuarioRepo.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!u.getRole().equals("ADMIN") && !u.getId().equals(c.getUsuarioId())) {
            throw new RuntimeException("Sem permissão para excluir");
        }

        repo.delete(c);
    }
}
