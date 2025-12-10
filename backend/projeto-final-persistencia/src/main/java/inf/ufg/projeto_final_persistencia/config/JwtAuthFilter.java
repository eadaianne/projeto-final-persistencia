package inf.ufg.projeto_final_persistencia.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Filtro JWT que:
 *  - pula totalmente rotas públicas (/api/auth/**)
 *  - valida token quando presente
 *  - carrega UserDetails e normaliza authorities para o formato ROLE_*
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = uds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        try {
            String path = req.getRequestURI();
            // libera totalmente endpoints de autenticação/registro para evitar interferência
            if (path != null && path.startsWith("/api/auth")) {
                chain.doFilter(req, res);
                return;
            }

            String header = req.getHeader("Authorization");
            String token = null;
            if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
                token = header.substring(7);
            }

            if (token != null && jwtUtil.validateToken(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                String username = jwtUtil.extractUsername(token);
                if (username != null) {
                    UserDetails ud = userDetailsService.loadUserByUsername(username);

                    // Normaliza authorities: garante prefixo ROLE_ quando necessário
                    Collection<? extends GrantedAuthority> original = ud.getAuthorities();
                    List<GrantedAuthority> normalized = original.stream()
                            .filter(Objects::nonNull)
                            .map(a -> {
                                String auth = a.getAuthority();
                                if (auth == null) return null;
                                auth = auth.trim();
                                if (auth.isEmpty()) return null;
                                if (auth.startsWith("ROLE_")) return new SimpleGrantedAuthority(auth);
                                return new SimpleGrantedAuthority("ROLE_" + auth);
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(ud, null, normalized);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception ex) {
            // não interromper a chain por problemas no token — só limpa o contexto e continua
            logger.warn("JWT filter error: " + ex.getMessage());
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(req, res);
    }
}
