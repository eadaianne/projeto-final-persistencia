package inf.ufg.projeto_final_persistencia.repositories;

import inf.ufg.projeto_final_persistencia.entities.Favorito;
import inf.ufg.projeto_final_persistencia.entities.FavoritoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, FavoritoId> {

    List<Favorito> findByIdUsuarioId(Long usuarioId);

    Optional<Favorito> findByIdUsuarioIdAndIdPontoId(Long usuarioId, Long pontoId);
}
