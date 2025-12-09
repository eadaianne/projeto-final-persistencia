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
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + (u.getRole() == null ? "USER" : u.getRole())));
        return new org.springframework.security.core.userdetails.User(u.getLogin(), u.getSenhaHash(), authorities);
    }
}
