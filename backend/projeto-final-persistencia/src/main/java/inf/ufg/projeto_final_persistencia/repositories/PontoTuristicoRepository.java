package inf.ufg.projeto_final_persistencia.repositories;

import inf.ufg.projeto_final_persistencia.entities.PontoTuristico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PontoTuristicoRepository extends JpaRepository<PontoTuristico, Long> {
}
