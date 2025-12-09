package inf.ufg.projeto_final_persistencia.dtos.export;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class PontoExportDTO {
    private Long id; // opcional no import, usado apenas para referência
    private String nome;
    private String descricao;
    private String cidade;
    private String estado;
    private String pais;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String endereco;
    private BigDecimal notaMedia;
    private Integer qtdAvaliacoes;
    private Instant createdAt;

    private List<HospedagemExportDTO> hospedagens;
    private List<AvaliacaoExportDTO> avaliacoes;
}
