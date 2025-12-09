package inf.ufg.projeto_final_persistencia.repositories;

import inf.ufg.projeto_final_persistencia.entities.PontoTuristico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface PontoTuristicoRepository extends JpaRepository<PontoTuristico, Long> {

    Page<PontoTuristico> findByCidadeIgnoreCase(String cidade, Pageable pageable);

    Page<PontoTuristico> findByNotaMediaGreaterThanEqual(BigDecimal nota, Pageable pageable);

    Page<PontoTuristico> findByCidadeIgnoreCaseAndNotaMediaGreaterThanEqual(String cidade, BigDecimal nota, Pageable pageable);

    Page<PontoTuristico> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
