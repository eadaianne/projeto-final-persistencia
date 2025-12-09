package inf.ufg.projeto_final_persistencia.dtos;

public record AvaliacaoDTO(
    Long id, Long pontoId, Long usuarioId, Integer nota, String comentario, java.time.Instant createdAt
) {}