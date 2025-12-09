package inf.ufg.projeto_final_persistencia.dtos;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record CreatePontoDTO(
    @NotBlank String nome,
    @NotBlank String descricao,
    @NotBlank String cidade,
    String estado,
    String pais,
    BigDecimal latitude,
    BigDecimal longitude,
    String endereco
) {}

