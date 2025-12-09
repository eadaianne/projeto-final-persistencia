package inf.ufg.projeto_final_persistencia.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "usuario_favorito")
@Data
public class Favorito {

    @EmbeddedId
    private FavoritoId id;

    @Column(name = "favorited_at")
    private Instant favoritedAt = Instant.now();
}
