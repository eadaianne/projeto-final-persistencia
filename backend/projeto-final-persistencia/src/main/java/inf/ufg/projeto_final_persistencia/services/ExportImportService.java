package inf.ufg.projeto_final_persistencia.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import inf.ufg.projeto_final_persistencia.dtos.export.AvaliacaoExportDTO;
import inf.ufg.projeto_final_persistencia.dtos.export.HospedagemExportDTO;
import inf.ufg.projeto_final_persistencia.dtos.export.PontoExportDTO;
import inf.ufg.projeto_final_persistencia.entities.Avaliacao;
import inf.ufg.projeto_final_persistencia.entities.Hospedagem;
import inf.ufg.projeto_final_persistencia.entities.PontoTuristico;
import inf.ufg.projeto_final_persistencia.entities.Usuario;
import inf.ufg.projeto_final_persistencia.repositories.PontoTuristicoRepository;
import inf.ufg.projeto_final_persistencia.repositories.HospedagemRepository;
import inf.ufg.projeto_final_persistencia.repositories.AvaliacaoRepository;
import inf.ufg.projeto_final_persistencia.repositories.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportImportService {

    @Autowired
    private PontoTuristicoRepository pontoRepo;

    @Autowired
    private HospedagemRepository hospRepo;

    @Autowired
    private AvaliacaoRepository avalRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private EntityManager em;

    private final ObjectMapper objectMapper;

    public ExportImportService() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public void exportAll(OutputStream out) throws Exception {
        List<PontoTuristico> pontos = pontoRepo.findAll();

        List<PontoExportDTO> export = pontos.stream().map(this::toPontoDTO).collect(Collectors.toList());

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, export);
    }

    private PontoExportDTO toPontoDTO(PontoTuristico p) {
        PontoExportDTO dto = new PontoExportDTO();
        dto.setId(p.getId());
        dto.setNome(p.getNome());
        dto.setDescricao(p.getDescricao());
        dto.setCidade(p.getCidade());
        dto.setEstado(p.getEstado());
        dto.setPais(p.getPais());
        dto.setLatitude(p.getLatitude());
        dto.setLongitude(p.getLongitude());
        dto.setEndereco(p.getEndereco());
        dto.setNotaMedia(p.getNotaMedia());
        dto.setQtdAvaliacoes(p.getQtdAvaliacoes());
        dto.setCreatedAt(p.getCreatedAt());

        TypedQuery<Hospedagem> hq = em.createQuery(
                "SELECT h FROM Hospedagem h WHERE h.ponto.id = :pontoId", Hospedagem.class);
        hq.setParameter("pontoId", p.getId());
        List<HospedagemExportDTO> hs = hq.getResultStream().map(h -> {
            HospedagemExportDTO hd = new HospedagemExportDTO();
            hd.setId(h.getId());
            hd.setNome(h.getNome());
            hd.setEndereco(h.getEndereco());
            hd.setTelefone(h.getTelefone());
            hd.setPrecoMedio(h.getPrecoMedio());
            hd.setTipo(h.getTipo());
            hd.setLinkReserva(h.getLinkReserva());
            return hd;
        }).collect(Collectors.toList());
        dto.setHospedagens(hs);

        TypedQuery<Avaliacao> aq = em.createQuery(
                "SELECT a FROM Avaliacao a WHERE a.ponto.id = :pontoId ORDER BY a.createdAt DESC", Avaliacao.class);
        aq.setParameter("pontoId", p.getId());
        List<AvaliacaoExportDTO> avs = aq.getResultStream().map(a -> {
            AvaliacaoExportDTO ad = new AvaliacaoExportDTO();
            ad.setId(a.getId());
            if (a.getUsuario() != null) {
                ad.setUsuarioId(a.getUsuario().getId());
                ad.setUsuarioLogin(a.getUsuario().getLogin());
            }
            ad.setNota(a.getNota());
            ad.setComentario(a.getComentario());
            ad.setCreatedAt(a.getCreatedAt());
            return ad;
        }).collect(Collectors.toList());
        dto.setAvaliacoes(avs);

        return dto;
    }

    @Transactional
    public void importFrom(InputStream in) throws Exception {
        PontoExportDTO[] pontos = objectMapper.readValue(in, PontoExportDTO[].class);

        for (PontoExportDTO pDto : pontos) {
            // Upsert by unique constraint (nome + cidade)
            PontoTuristico existing = pontoRepo.findByNomeAndCidadeIgnoreCase(pDto.getNome(), pDto.getCidade())
                    .orElse(null);

            PontoTuristico p;
            if (existing == null) {
                p = new PontoTuristico();
            } else {
                p = existing;
            }

            p.setNome(pDto.getNome());
            p.setDescricao(pDto.getDescricao());
            p.setCidade(pDto.getCidade());
            p.setEstado(pDto.getEstado());
            p.setPais(pDto.getPais());
            p.setLatitude(pDto.getLatitude());
            p.setLongitude(pDto.getLongitude());
            p.setEndereco(pDto.getEndereco());

            PontoTuristico saved = pontoRepo.save(p);

            if (pDto.getHospedagens() != null) {
                for (HospedagemExportDTO hDto : pDto.getHospedagens()) {
                    if (hDto == null || hDto.getNome() == null) continue;

                    TypedQuery<Hospedagem> hFind = em.createQuery(
                            "SELECT h FROM Hospedagem h WHERE h.ponto.id = :pontoId AND lower(h.nome) = :nome",
                            Hospedagem.class);
                    hFind.setParameter("pontoId", saved.getId());
                    hFind.setParameter("nome", hDto.getNome().toLowerCase());

                    List<Hospedagem> foundHs = hFind.getResultList();
                    if (!foundHs.isEmpty()) {
                        Hospedagem hExist = foundHs.get(0);
                        hExist.setEndereco(hDto.getEndereco());
                        hExist.setTelefone(hDto.getTelefone());
                        hExist.setPrecoMedio(hDto.getPrecoMedio());
                        hExist.setTipo(hDto.getTipo());
                        hExist.setLinkReserva(hDto.getLinkReserva());
                        hospRepo.save(hExist);
                    } else {
                        Hospedagem h = new Hospedagem();
                        h.setPonto(saved);
                        h.setNome(hDto.getNome());
                        h.setEndereco(hDto.getEndereco());
                        h.setTelefone(hDto.getTelefone());
                        h.setPrecoMedio(hDto.getPrecoMedio());
                        h.setTipo(hDto.getTipo());
                        h.setLinkReserva(hDto.getLinkReserva());
                        hospRepo.save(h);
                    }
                }
            }

            
            if (pDto.getAvaliacoes() != null) {
                for (AvaliacaoExportDTO aDto : pDto.getAvaliacoes()) {
                    if (aDto == null) continue;
                    Long usuarioId = aDto.getUsuarioId();
                    if (usuarioId == null) continue;

            
                    Usuario usuario = usuarioRepo.findById(usuarioId).orElse(null);
                    if (usuario == null) continue;

                    TypedQuery<Long> existsQuery = em.createQuery(
                            "SELECT COUNT(a) FROM Avaliacao a WHERE a.ponto.id = :pontoId AND a.usuario.id = :usuarioId",
                            Long.class);
                    existsQuery.setParameter("pontoId", saved.getId());
                    existsQuery.setParameter("usuarioId", usuarioId);
                    Long count = existsQuery.getSingleResult();
                    if (count != null && count > 0) continue;

                    Avaliacao a = new Avaliacao();
                    a.setPonto(saved);
                    a.setUsuario(usuario);
                    a.setNota(aDto.getNota());
                    a.setComentario(aDto.getComentario());
                    avalRepo.save(a);
                }
            }
            
        }
    }
}
