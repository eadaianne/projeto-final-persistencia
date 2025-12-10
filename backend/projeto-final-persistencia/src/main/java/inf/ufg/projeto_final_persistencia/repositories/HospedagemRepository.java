package inf.ufg.projeto_final_persistencia.repositories;

import inf.ufg.projeto_final_persistencia.entities.Hospedagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HospedagemRepository extends JpaRepository<Hospedagem, Long> {
    List<Hospedagem> findByPontoId(Long pontoId);

    Optional<Hospedagem> findByPontoIdAndNomeIgnoreCase(Long pontoId, String nome);
}
