package inf.ufg.projeto_final_persistencia.services;

import inf.ufg.projeto_final_persistencia.config.JwtUtil;
import inf.ufg.projeto_final_persistencia.dtos.AuthDTOs.*;
import inf.ufg.projeto_final_persistencia.entities.Usuario;
import inf.ufg.projeto_final_persistencia.repositories.UsuarioRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder, AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest req) {
        if (usuarioRepo.findByLogin(req.login()).isPresent()) throw new IllegalArgumentException("Login já existe");
        if (usuarioRepo.findByEmail(req.email()).isPresent()) throw new IllegalArgumentException("Email já existe");

        Usuario u = Usuario.builder()
                .login(req.login())
                .email(req.email())
                .senhaHash(passwordEncoder.encode(req.password()))
                .role("USER")
                .build();
        usuarioRepo.save(u);
    }

    public String login(AuthRequest req) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.loginOrEmail(), req.password()));
        // se chegar aqui, está autenticado
        String username = auth.getName(); // this will be the login
        return jwtUtil.generateToken(username);
    }
}
