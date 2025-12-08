package inf.ufg.projeto_final_persistencia.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record PontoDTO(
    Long id,
    String nome,
    String descricao,
    String cidade,
    String estado,
    String pais,
    BigDecimal latitude,
    BigDecimal longitude,
    String endereco,
    BigDecimal notaMedia,
    Integer qtdAvaliacoes,
    Instant createdAt
) {}
