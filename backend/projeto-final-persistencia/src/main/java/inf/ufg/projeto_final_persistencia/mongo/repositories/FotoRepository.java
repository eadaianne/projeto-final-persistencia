package inf.ufg.projeto_final_persistencia.mongo.repositories;

import inf.ufg.projeto_final_persistencia.mongo.entities.Foto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FotoRepository extends MongoRepository<Foto, String> {

    List<Foto> findByPontoIdOrderByCreatedAtDesc(Long pontoId);
}
