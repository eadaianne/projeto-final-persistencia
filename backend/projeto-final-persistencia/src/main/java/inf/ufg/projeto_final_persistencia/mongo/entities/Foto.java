package inf.ufg.projeto_final_persistencia.mongo.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "fotos")
public class Foto {

    @Id
    private String id;

    private Long pontoId;
    private Long usuarioId;

    private String filename;
    private String url;
    private String contentType;

    private Instant createdAt = Instant.now();
}
