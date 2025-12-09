package inf.ufg.projeto_final_persistencia.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class FavoritoDTO {
    private Long usuarioId;
    private Long pontoId;
    private Instant favoritedAt;
}
