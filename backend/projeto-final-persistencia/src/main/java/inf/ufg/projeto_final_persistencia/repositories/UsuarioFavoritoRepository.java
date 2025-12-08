package inf.ufg.projeto_final_persistencia.repositories;

import inf.ufg.projeto_final_persistencia.entities.UsuarioFavorito;
import inf.ufg.projeto_final_persistencia.entities.UsuarioFavoritoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioFavoritoRepository extends JpaRepository<UsuarioFavorito, UsuarioFavoritoId> {
}
