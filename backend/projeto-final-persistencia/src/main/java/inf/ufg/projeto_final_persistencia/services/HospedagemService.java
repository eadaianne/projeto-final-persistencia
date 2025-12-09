package inf.ufg.projeto_final_persistencia.services;

import inf.ufg.projeto_final_persistencia.dtos.CreateHospedagemDTO;
import inf.ufg.projeto_final_persistencia.dtos.HospedagemDTO;
import inf.ufg.projeto_final_persistencia.entities.Hospedagem;
import inf.ufg.projeto_final_persistencia.entities.PontoTuristico;
import inf.ufg.projeto_final_persistencia.repositories.HospedagemRepository;
import inf.ufg.projeto_final_persistencia.repositories.PontoTuristicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospedagemService {

    @Autowired
    private HospedagemRepository hospedagemRepo;

    @Autowired
    private PontoTuristicoRepository pontoRepo;

    public HospedagemDTO create(CreateHospedagemDTO dto) {
        PontoTuristico p = pontoRepo.findById(dto.pontoId()).orElseThrow(() -> new IllegalArgumentException("Ponto não encontrado"));
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
        return new HospedagemDTO(saved.getId(), saved.getPonto().getId(), saved.getNome(), saved.getEndereco(), saved.getTelefone(), saved.getPrecoMedio(), saved.getTipo(), saved.getLinkReserva());
    }

    public List<HospedagemDTO> findByPonto(Long pontoId) {
        return hospedagemRepo.findByPontoId(pontoId).stream()
                .map(h -> new HospedagemDTO(h.getId(), h.getPonto().getId(), h.getNome(), h.getEndereco(), h.getTelefone(), h.getPrecoMedio(), h.getTipo(), h.getLinkReserva()))
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        hospedagemRepo.deleteById(id);
    }
}
