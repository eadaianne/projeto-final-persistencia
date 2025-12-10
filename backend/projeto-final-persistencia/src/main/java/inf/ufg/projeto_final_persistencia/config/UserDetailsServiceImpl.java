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
        Usuario u = usuarioRepo.findByLogin(usernameOrEmail)
                .orElseGet(() -> usuarioRepo.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado")));

        // normaliza a role: remove prefixo ROLE_ se já existir, usa USER se nulo
        String rawRole = u.getRole();
        if (rawRole == null || rawRole.trim().isEmpty()) {
            rawRole = "USER";
        }
        rawRole = rawRole.trim();
        if (rawRole.startsWith("ROLE_")) {
            rawRole = rawRole.substring("ROLE_".length());
        }

        String roleWithPrefix = "ROLE_" + rawRole; // garante apenas um ROLE_ no começo

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleWithPrefix));

        return new org.springframework.security.core.userdetails.User(
                u.getLogin(),
                u.getSenhaHash(),
                authorities
        );
    }
}
