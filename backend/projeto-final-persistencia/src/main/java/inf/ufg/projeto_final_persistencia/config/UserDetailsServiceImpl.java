package inf.ufg.projeto_final_persistencia.config;

import inf.ufg.projeto_final_persistencia.entities.Usuario;
import inf.ufg.projeto_final_persistencia.repositories.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        // busca por login ou por email
        Usuario u = usuarioRepo.findByLogin(usernameOrEmail)
                .orElseGet(() -> usuarioRepo.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado")));

        // garante que a ROLE sempre exista
        String role = u.getRole() == null ? "USER" : u.getRole();

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + role));

        // retorna o usuário para o Spring Security
        return new org.springframework.security.core.userdetails.User(
                u.getLogin(),            // nome de login usado no token
                u.getSenhaHash(),        // senha já hasheada
                authorities              // roles
        );
    }
}
