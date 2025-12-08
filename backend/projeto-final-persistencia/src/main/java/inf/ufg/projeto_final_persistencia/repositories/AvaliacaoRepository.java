package inf.ufg.projeto_final_persistencia.repositories;

import inf.ufg.projeto_final_persistencia.entities.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
}
