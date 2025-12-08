package inf.ufg.projeto_final_persistencia.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateHospedagemDTO(
    Long pontoId,
    @NotBlank @Size(max = 255) String nome,
    String endereco,
    String telefone,
    BigDecimal precoMedio,
    String tipo,
    String linkReserva
) {}