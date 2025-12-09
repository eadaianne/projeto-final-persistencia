package inf.ufg.projeto_final_persistencia.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class FavoritoId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "ponto_id")
    private Long pontoId;
}
