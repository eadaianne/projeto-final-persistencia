package inf.ufg.projeto_final_persistencia.services;

import inf.ufg.projeto_final_persistencia.dtos.CreatePontoDTO;
import inf.ufg.projeto_final_persistencia.dtos.PontoDTO;
import inf.ufg.projeto_final_persistencia.dtos.UpdatePontoDTO;
import inf.ufg.projeto_final_persistencia.entities.PontoTuristico;
import inf.ufg.projeto_final_persistencia.entities.Usuario;
import inf.ufg.projeto_final_persistencia.repositories.PontoTuristicoRepository;
import inf.ufg.projeto_final_persistencia.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PontoService {

    @Autowired
    private PontoTuristicoRepository pontoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    public Page<PontoDTO> list(String cidade, BigDecimal notaMin, String nome, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("notaMedia").descending());
        Page<PontoTuristico> p;

        if (nome != null && !nome.isBlank()) {
            p = pontoRepo.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (cidade != null && notaMin != null) {
            p = pontoRepo.findByCidadeIgnoreCaseAndNotaMediaGreaterThanEqual(cidade, notaMin, pageable);
        } else if (cidade != null) {
            p = pontoRepo.findByCidadeIgnoreCase(cidade, pageable);
        } else if (notaMin != null) {
            p = pontoRepo.findByNotaMediaGreaterThanEqual(notaMin, pageable);
        } else {
            p = pontoRepo.findAll(pageable);
        }

        return p.map(this::toDto);
    }

    public Optional<PontoDTO> getById(Long id) {
        return pontoRepo.findById(id).map(this::toDto);
    }

    @Transactional
    public PontoDTO create(CreatePontoDTO dto, String usernameLogado) {

        Usuario criador = usuarioRepo.findByLogin(usernameLogado)
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido"));

        PontoTuristico p = new PontoTuristico();
        p.setNome(dto.nome());
        p.setDescricao(dto.descricao());
        p.setCidade(dto.cidade());
        p.setEstado(dto.estado());
        p.setPais(dto.pais());
        p.setLatitude(dto.latitude());
        p.setLongitude(dto.longitude());
        p.setEndereco(dto.endereco());
        p.setCriadoPor(criador);

        return toDto(pontoRepo.save(p));
    }

    @Transactional
    public PontoDTO update(Long id, UpdatePontoDTO dto, String usernameLogado) {

        PontoTuristico p = pontoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ponto não encontrado"));

        Usuario user = usuarioRepo.findByLogin(usernameLogado)
                .orElseThrow();

        boolean isOwner = p.getCriadoPor() != null && p.getCriadoPor().getLogin().equals(user.getLogin());
        boolean isAdmin = user.getRole().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Você não tem permissão para editar este ponto.");
        }

        if (dto.descricao() != null)
            p.setDescricao(dto.descricao());
        if (dto.cidade() != null)
            p.setCidade(dto.cidade());
        if (dto.estado() != null)
            p.setEstado(dto.estado());
        if (dto.pais() != null)
            p.setPais(dto.pais());
        if (dto.latitude() != null)
            p.setLatitude(dto.latitude());
        if (dto.longitude() != null)
            p.setLongitude(dto.longitude());
        if (dto.endereco() != null)
            p.setEndereco(dto.endereco());

        PontoTuristico saved = pontoRepo.save(p);
        return toDto(saved);
    }

    @Transactional
    public void delete(Long id, String usernameLogado) {
        PontoTuristico p = pontoRepo.findById(id).orElseThrow();
        Usuario user = usuarioRepo.findByLogin(usernameLogado).orElseThrow();

        boolean isOwner = p.getCriadoPor().getLogin().equals(user.getLogin());
        boolean isAdmin = "ADMIN".equals(user.getRole());

        if (!isOwner && !isAdmin)
            throw new AccessDeniedException("Você não pode excluir este ponto");

        pontoRepo.delete(p);
    }

    private PontoDTO toDto(PontoTuristico p) {
        return new PontoDTO(
                p.getId(),
                p.getNome(),
                p.getDescricao(),
                p.getCidade(),
                p.getEstado(),
                p.getPais(),
                p.getLatitude(),
                p.getLongitude(),
                p.getEndereco(),
                p.getNotaMedia(),
                p.getQtdAvaliacoes(),
                p.getCreatedAt());
    }
}
