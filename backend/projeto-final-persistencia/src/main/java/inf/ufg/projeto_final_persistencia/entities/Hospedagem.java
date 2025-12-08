package inf.ufg.projeto_final_persistencia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "hospedagem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospedagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_id", nullable = false)
    private PontoTuristico ponto;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String endereco;

    @Column(length = 50)
    private String telefone;

    @Column(name = "preco_medio", precision = 10, scale = 2)
    private java.math.BigDecimal precoMedio;

    @Column(length = 30)
    private String tipo;

    @Column(name = "link_reserva")
    private String linkReserva;
}
