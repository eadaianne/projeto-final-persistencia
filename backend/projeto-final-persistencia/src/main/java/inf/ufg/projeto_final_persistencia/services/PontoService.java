package inf.ufg.projeto_final_persistencia.services;

import inf.ufg.projeto_final_persistencia.dtos.CreatePontoDTO;
import inf.ufg.projeto_final_persistencia.dtos.PontoDTO;
import inf.ufg.projeto_final_persistencia.dtos.UpdatePontoDTO;
import inf.ufg.projeto_final_persistencia.entities.PontoTuristico;
import inf.ufg.projeto_final_persistencia.entities.Usuario;
import inf.ufg.projeto_final_persistencia.repositories.PontoTuristicoRepository;
import inf.ufg.projeto_final_persistencia.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
    public PontoDTO create(CreatePontoDTO dto) {
        PontoTuristico p = new PontoTuristico();
        p.setNome(dto.nome());
        p.setDescricao(dto.descricao());
        p.setCidade(dto.cidade());
        p.setEstado(dto.estado());
        p.setPais(dto.pais());
        p.setLatitude(dto.latitude());
        p.setLongitude(dto.longitude());
        p.setEndereco(dto.endereco());

        if (dto.criadoPorId() != null) {
            Usuario u = usuarioRepo.findById(dto.criadoPorId()).orElse(null);
            p.setCriadoPor(u);
        }
        PontoTuristico saved = pontoRepo.save(p);
        return toDto(saved);
    }

    @Transactional
    public Optional<PontoDTO> update(Long id, UpdatePontoDTO dto) {
        return pontoRepo.findById(id).map(p -> {
            if (dto.nome() != null) p.setNome(dto.nome());
            if (dto.descricao() != null) p.setDescricao(dto.descricao());
            if (dto.cidade() != null) p.setCidade(dto.cidade());
            if (dto.estado() != null) p.setEstado(dto.estado());
            if (dto.pais() != null) p.setPais(dto.pais());
            if (dto.latitude() != null) p.setLatitude(dto.latitude());
            if (dto.longitude() != null) p.setLongitude(dto.longitude());
            if (dto.endereco() != null) p.setEndereco(dto.endereco());
            return toDto(pontoRepo.save(p));
        });
    }

    @Transactional
    public void delete(Long id) {
        pontoRepo.deleteById(id);
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
            p.getCreatedAt()
        );
    }
}
