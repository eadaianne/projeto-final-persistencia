package inf.ufg.projeto_final_persistencia.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import inf.ufg.projeto_final_persistencia.dtos.AuthDTOs.AuthRequest;
import inf.ufg.projeto_final_persistencia.dtos.AuthDTOs.AuthResponse;
import inf.ufg.projeto_final_persistencia.dtos.AuthDTOs.RegisterRequest;
import inf.ufg.projeto_final_persistencia.entities.Usuario;
import inf.ufg.projeto_final_persistencia.services.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        String token = authService.login(req);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> getMe(@AuthenticationPrincipal UserDetails userDetails) {

        // Caso não esteja autenticado
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        Usuario usuario = authService.getUsuarioByLogin(userDetails.getUsername());

        if (usuario == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(usuario);
    }
}

