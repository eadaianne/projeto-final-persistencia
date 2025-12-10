package inf.ufg.projeto_final_persistencia.dtos.export;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class HospedagemExportDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private BigDecimal precoMedio;
    private String tipo;
    private String linkReserva;
    private Instant createdAt;
}
