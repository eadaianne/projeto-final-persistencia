package inf.ufg.projeto_final_persistencia.dtos;


import java.math.BigDecimal;

public record UpdatePontoDTO(
    String descricao,
    String cidade,
    String estado,
    String pais,
    BigDecimal latitude,
    BigDecimal longitude,
    String endereco
) {}
