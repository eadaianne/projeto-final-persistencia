package inf.ufg.projeto_final_persistencia.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateHospedagemDTO(
    @NotNull Long pontoId,
    @NotBlank String nome,
    String endereco,
    String telefone,
    BigDecimal precoMedio,
    String tipo,
    String linkReserva
) {}
