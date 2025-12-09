package inf.ufg.projeto_final_persistencia.mongo.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class ComentarioDTO {
    private String id;
    private Long pontoId;
    private Long usuarioId;
    private String texto;
    private Instant createdAt;
}
