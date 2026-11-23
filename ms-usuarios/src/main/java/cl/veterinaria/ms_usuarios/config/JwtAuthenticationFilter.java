package cl.veterinaria.ms_usuarios.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                UsuarioAutenticado usuario = jwtTokenService.validar(authorization.substring(7));
                String rol = usuario.rol().toUpperCase(Locale.ROOT).replaceFirst("^ROLE_", "");
                String autoridad = "ADMIN".equals(rol) || "ADMINISTRADOR".equals(rol)
                        ? "ROLE_ADMIN"
                        : "ROLE_" + rol;
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                usuario, null, List.of(new SimpleGrantedAuthority(autoridad))
                        )
                );
            } catch (JwtException | IllegalArgumentException exception) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"El token es inválido o expiró.\",\"fieldErrors\":{}}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
