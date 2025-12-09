package inf.ufg.projeto_final_persistencia.services;

import inf.ufg.projeto_final_persistencia.dtos.FavoritoDTO;
import inf.ufg.projeto_final_persistencia.entities.Favorito;
import inf.ufg.projeto_final_persistencia.entities.FavoritoId;
import inf.ufg.projeto_final_persistencia.entities.Usuario;
import inf.ufg.projeto_final_persistencia.repositories.FavoritoRepository;
import inf.ufg.projeto_final_persistencia.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository repo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    private FavoritoDTO toDto(Favorito f) {
        FavoritoDTO dto = new FavoritoDTO();
        dto.setUsuarioId(f.getId().getUsuarioId());
        dto.setPontoId(f.getId().getPontoId());
        dto.setFavoritedAt(f.getFavoritedAt());
        return dto;
    }

    @Transactional
    public FavoritoDTO marcarFavorito(Long pontoId, String username) {
        Usuario u = usuarioRepo.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // verifica existência
        repo.findByIdUsuarioIdAndIdPontoId(u.getId(), pontoId)
                .ifPresent(f -> { throw new RuntimeException("Já favoritado"); });

        Favorito f = new Favorito();
        FavoritoId id = new FavoritoId();
        id.setUsuarioId(u.getId());
        id.setPontoId(pontoId);
        f.setId(id);
        // favoritedAt já setado por default
        repo.save(f);
        return toDto(f);
    }

    @Transactional
    public void removerFavorito(Long pontoId, String username) {
        Usuario u = usuarioRepo.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Favorito fav = repo.findByIdUsuarioIdAndIdPontoId(u.getId(), pontoId)
                .orElseThrow(() -> new RuntimeException("Favorito não existe"));

        repo.delete(fav);
    }

    public List<FavoritoDTO> listarMeusFavoritos(String username) {
        Usuario u = usuarioRepo.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return repo.findByIdUsuarioId(u.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }
}
