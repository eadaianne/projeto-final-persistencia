package inf.ufg.projeto_final_persistencia.mongo.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "comentarios")
public class Comentario {

    @Id
    private String id;

    private Long pontoId;
    private Long usuarioId;
    private String texto;
    private Instant createdAt = Instant.now();

    private String device;
    private String language;
}
