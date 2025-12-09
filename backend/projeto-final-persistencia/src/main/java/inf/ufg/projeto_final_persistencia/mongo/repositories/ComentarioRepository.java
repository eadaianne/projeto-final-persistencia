package inf.ufg.projeto_final_persistencia.mongo.repositories;

import inf.ufg.projeto_final_persistencia.mongo.entities.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ComentarioRepository extends MongoRepository<Comentario, String> {

    List<Comentario> findByPontoIdOrderByCreatedAtDesc(Long pontoId);
}
