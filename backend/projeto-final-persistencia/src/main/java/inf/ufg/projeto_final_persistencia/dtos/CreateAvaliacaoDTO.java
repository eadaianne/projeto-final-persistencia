package inf.ufg.projeto_final_persistencia.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAvaliacaoDTO(
    @NotNull Long pontoId,
    @Min(1) @Max(5) Integer nota,
    @Size(max = 500) String comentario
) {}

