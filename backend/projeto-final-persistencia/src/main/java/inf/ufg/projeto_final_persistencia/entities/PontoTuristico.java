package inf.ufg.projeto_final_persistencia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ponto_turistico",
       uniqueConstraints = @UniqueConstraint(columnNames = {"nome", "cidade"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PontoTuristico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String nome;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String cidade;

    @Size(max = 80)
    private String estado;

    @Size(max = 80)
    private String pais;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(columnDefinition = "TEXT")
    private String endereco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por")
    private Usuario criadoPor;

    @Column(name = "nota_media", precision = 3, scale = 2, nullable = false)
    private BigDecimal notaMedia = BigDecimal.valueOf(0.0);

    @Column(name = "qtd_avaliacoes", nullable = false)
    private Integer qtdAvaliacoes = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "ponto", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<Hospedagem> hospedagens = new ArrayList<>();

    @OneToMany(mappedBy = "ponto", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }
}