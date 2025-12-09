package inf.ufg.projeto_final_persistencia.mongo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateComentarioDTO {

    private Long pontoId;

    @NotBlank
    @Size(max = 500)
    private String texto;

    private String device;
    private String language;
}
