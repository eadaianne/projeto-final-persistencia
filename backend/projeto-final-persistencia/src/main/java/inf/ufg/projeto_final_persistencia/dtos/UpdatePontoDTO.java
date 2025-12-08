package inf.ufg.projeto_final_persistencia.dtos;

import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdatePontoDTO(
    @Size(max = 255) String nome,
    String descricao,
    @Size(max = 120) String cidade,
    String estado,
    String pais,
    BigDecimal latitude,
    BigDecimal longitude,
    String endereco
) {}
