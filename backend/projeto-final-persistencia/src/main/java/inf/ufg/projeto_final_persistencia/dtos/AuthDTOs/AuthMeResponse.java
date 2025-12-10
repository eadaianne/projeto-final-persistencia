package inf.ufg.projeto_final_persistencia.dtos.AuthDTOs;

public record AuthMeResponse(
        Long id,
        String login,
        String email,
        String role
) {}
