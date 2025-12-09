package inf.ufg.projeto_final_persistencia.mongo.services;

import inf.ufg.projeto_final_persistencia.entities.Usuario;
import inf.ufg.projeto_final_persistencia.mongo.dtos.FotoDTO;
import inf.ufg.projeto_final_persistencia.mongo.entities.Foto;
import inf.ufg.projeto_final_persistencia.mongo.repositories.FotoRepository;
import inf.ufg.projeto_final_persistencia.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UploadService {

    private final Path uploadDir = Paths.get("uploads");

    @Autowired
    private FotoRepository repo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    public FotoDTO toDto(Foto f) {
        FotoDTO dto = new FotoDTO();
        dto.setId(f.getId());
        dto.setPontoId(f.getPontoId());
        dto.setUsuarioId(f.getUsuarioId());
        dto.setFilename(f.getFilename());
        dto.setContentType(f.getContentType());
        dto.setUrl(f.getUrl());
        dto.setCreatedAt(f.getCreatedAt());
        return dto;
    }

    public FotoDTO uploadFile(Long pontoId, MultipartFile file, String username) {
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Usuario u = usuarioRepo.findByLogin(username)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filepath = uploadDir.resolve(filename);

            Files.copy(file.getInputStream(), filepath);

            Foto f = new Foto();
            f.setPontoId(pontoId);
            f.setUsuarioId(u.getId());
            f.setFilename(filename);
            f.setContentType(file.getContentType());
            f.setUrl("/uploads/" + filename);

            repo.save(f);
            return toDto(f);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload: " + e.getMessage());
        }
    }

    public List<FotoDTO> listByPonto(Long pontoId) {
        return repo.findByPontoIdOrderByCreatedAtDesc(pontoId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public void delete(String id, String username) {
        Foto f = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        Usuario u = usuarioRepo.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!u.getRole().equals("ADMIN") && !f.getUsuarioId().equals(u.getId())) {
            throw new RuntimeException("Sem permissão para excluir");
        }

        try {
            Files.deleteIfExists(uploadDir.resolve(f.getFilename()));
        } catch (Exception ignored) {}

        repo.delete(f);
    }
}
