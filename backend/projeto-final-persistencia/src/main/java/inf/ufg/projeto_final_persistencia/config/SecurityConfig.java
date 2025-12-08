package inf.ufg.projeto_final_persistencia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth

            // auth pública
            .requestMatchers("/api/auth/**").permitAll()

            // GET públicos — conforme o enunciado
            .requestMatchers(HttpMethod.GET, "/api/pontos/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/hospedagens/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/avaliacoes/**").permitAll()

            // Export/Import — Sprint 3 (mas configurado desde já)
            .requestMatchers("/api/export/**", "/api/import/**").hasRole("ADMIN")

            // Demais rotas exigem autenticação
            .requestMatchers("/api/**").authenticated()

            .anyRequest().permitAll()
        );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
