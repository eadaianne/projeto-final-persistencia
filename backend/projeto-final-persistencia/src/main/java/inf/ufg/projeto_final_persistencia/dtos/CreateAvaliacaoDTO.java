package inf.ufg.projeto_final_persistencia.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateAvaliacaoDTO(
    @NotNull Long pontoId,
    @NotNull Long usuarioId,
    @Min(1) @Max(5) Integer nota,
    String comentario
) {}