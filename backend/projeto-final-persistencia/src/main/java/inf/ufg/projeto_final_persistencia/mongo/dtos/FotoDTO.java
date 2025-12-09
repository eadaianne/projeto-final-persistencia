package inf.ufg.projeto_final_persistencia.mongo.dtos;

import lombok.Data;
import java.time.Instant;

@Data
public class FotoDTO {
    private String id;
    private Long pontoId;
    private Long usuarioId;
    private String url;
    private String filename;
    private String contentType;
    private Instant createdAt;
}
