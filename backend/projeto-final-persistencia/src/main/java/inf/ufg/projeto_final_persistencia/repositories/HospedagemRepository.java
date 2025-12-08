package inf.ufg.projeto_final_persistencia.repositories;

import inf.ufg.projeto_final_persistencia.entities.Hospedagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedagemRepository extends JpaRepository<Hospedagem, Long> {
}
