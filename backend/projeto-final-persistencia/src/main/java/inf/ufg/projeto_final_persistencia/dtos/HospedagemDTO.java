package inf.ufg.projeto_final_persistencia.dtos;

import java.math.BigDecimal;

public record HospedagemDTO(
    Long id, Long pontoId, String nome, String endereco, String telefone, BigDecimal precoMedio, String tipo, String linkReserva
) {}
