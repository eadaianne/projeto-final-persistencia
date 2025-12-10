package inf.ufg.projeto_final_persistencia.services;

import inf.ufg.projeto_final_persistencia.dtos.CreateHospedagemDTO;
import inf.ufg.projeto_final_persistencia.dtos.HospedagemDTO;
import inf.ufg.projeto_final_persistencia.entities.Hospedagem;
import inf.ufg.projeto_final_persistencia.entities.PontoTuristico;
import inf.ufg.projeto_final_persistencia.entities.Usuario;
import inf.ufg.projeto_final_persistencia.repositories.HospedagemRepository;
import inf.ufg.projeto_final_persistencia.repositories.PontoTuristicoRepository;
import inf.ufg.projeto_final_persistencia.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospedagemService {

    @Autowired
    private HospedagemRepository hospedagemRepo;

    @Autowired
    private PontoTuristicoRepository pontoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    // -----------------------
    // CREATE (apenas ADMIN)
    // -----------------------
    public HospedagemDTO create(CreateHospedagemDTO dto, String usernameLogado) {

        Usuario user = usuarioRepo.findByLogin(usernameLogado)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!"ADMIN".equals(user.getRole())) {
            throw new AccessDeniedException("Apenas administradores podem cadastrar hospedagens.");
        }

        PontoTuristico p = pontoRepo.findById(dto.pontoId())
                .orElseThrow(() -> new EntityNotFoundException("Ponto turístico não encontrado."));

        Hospedagem h = Hospedagem.builder()
                .ponto(p)
                .nome(dto.nome())
                .endereco(dto.endereco())
                .telefone(dto.telefone())
                .precoMedio(dto.precoMedio())
                .tipo(dto.tipo())
                .linkReserva(dto.linkReserva())
                .build();

        Hospedagem saved = hospedagemRepo.save(h);

        return new HospedagemDTO(
                saved.getId(),
                saved.getPonto().getId(),
                saved.getNome(),
                saved.getEndereco(),
                saved.getTelefone(),
                saved.getPrecoMedio(),
                saved.getTipo(),
                saved.getLinkReserva()
        );
    }

    // -----------------------
    // LIST (aberto ao público)
    // -----------------------
    public List<HospedagemDTO> findByPonto(Long pontoId) {
        return hospedagemRepo.findByPontoId(pontoId)
                .stream()
                .map(h -> new HospedagemDTO(
                        h.getId(),
                        h.getPonto().getId(),
                        h.getNome(),
                        h.getEndereco(),
                        h.getTelefone(),
                        h.getPrecoMedio(),
                        h.getTipo(),
                        h.getLinkReserva()
                ))
                .collect(Collectors.toList());
    }

    // -----------------------
    // DELETE (apenas ADMIN)
    // -----------------------
    public void delete(Long id, String usernameLogado) {

        Usuario user = usuarioRepo.findByLogin(usernameLogado)
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido"));

        if (!"ADMIN".equals(user.getRole())) {
            throw new AccessDeniedException("Apenas administradores podem excluir hospedagens.");
        }

        if (!hospedagemRepo.existsById(id)) {
            throw new EntityNotFoundException("Hospedagem não encontrada.");
        }

        hospedagemRepo.deleteById(id);
    }
}
