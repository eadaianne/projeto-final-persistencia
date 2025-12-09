package inf.ufg.projeto_final_persistencia.dtos.export;

import lombok.Data;
import java.time.Instant;

@Data
public class AvaliacaoExportDTO {
    private Long id;
    private String usuarioLogin;
    private Long usuarioId;     
    private Integer nota;
    private String comentario;
    private Instant createdAt;
}
