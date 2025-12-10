package inf.ufg.projeto_final_persistencia.repositories;

import inf.ufg.projeto_final_persistencia.entities.Avaliacao;
import inf.ufg.projeto_final_persistencia.entities.PontoTuristico;
import inf.ufg.projeto_final_persistencia.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    Optional<Avaliacao> findByPontoAndUsuario(PontoTuristico ponto, Usuario usuario);
    List<Avaliacao> findByPontoOrderByCreatedAtDesc(PontoTuristico ponto);
    Optional<Avaliacao> findByPontoIdAndUsuarioId(Long pontoId, Long usuarioId);
    List<Avaliacao> findByPontoIdOrderByCreatedAtDesc(Long pontoId);
}