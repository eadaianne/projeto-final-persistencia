package inf.ufg.projeto_final_persistencia.repositories;

import inf.ufg.projeto_final_persistencia.entities.PontoTuristico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface PontoTuristicoRepository extends JpaRepository<PontoTuristico, Long> {

    Page<PontoTuristico> findByCidadeIgnoreCase(String cidade, Pageable pageable);

    Page<PontoTuristico> findByNotaMediaGreaterThanEqual(BigDecimal nota, Pageable pageable);

    Page<PontoTuristico> findByCidadeIgnoreCaseAndNotaMediaGreaterThanEqual(String cidade, BigDecimal nota, Pageable pageable);

    Page<PontoTuristico> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Optional<PontoTuristico> findByNomeAndCidadeIgnoreCase(String nome, String cidade);

    @Query("""
      SELECT p FROM PontoTuristico p
      WHERE (:cidade IS NULL OR LOWER(p.cidade) = LOWER(:cidade))
        AND (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%',:nome,'%')))
        AND (:notaMin IS NULL OR p.notaMedia >= :notaMin)
      """)
    Page<PontoTuristico> search(
        @Param("cidade") String cidade,
        @Param("nome") String nome,
        @Param("notaMin") BigDecimal notaMin,
        Pageable pageable
    );
}
