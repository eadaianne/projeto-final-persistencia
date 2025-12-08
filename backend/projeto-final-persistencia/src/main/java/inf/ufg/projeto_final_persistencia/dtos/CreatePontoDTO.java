package inf.ufg.projeto_final_persistencia.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreatePontoDTO(
    @NotBlank @Size(max = 255) String nome,
    @NotBlank String descricao,
    @NotBlank @Size(max = 120) String cidade,
    String estado,
    String pais,
    BigDecimal latitude,
    BigDecimal longitude,
    String endereco,
    Long criadoPorId
) {}
