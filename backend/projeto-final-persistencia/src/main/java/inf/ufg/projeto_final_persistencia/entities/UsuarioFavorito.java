package inf.ufg.projeto_final_persistencia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "usuario_favorito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioFavorito {

    @EmbeddedId
    private UsuarioFavoritoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pontoId")
    @JoinColumn(name = "ponto_id")
    private PontoTuristico ponto;

    @Column(name = "favorited_at", nullable = false)
    private Instant favoritedAt;

    @PrePersist
    public void prePersist() {
        if (favoritedAt == null) favoritedAt = Instant.now();
    }
}