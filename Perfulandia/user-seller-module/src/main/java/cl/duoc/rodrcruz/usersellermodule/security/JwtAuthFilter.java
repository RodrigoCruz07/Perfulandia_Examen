package cl.duoc.rodrcruz.usersellermodule.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        if (path.startsWith("/api/auth/") || path.startsWith("/api/purchases/")
                || path.startsWith("/h2-console/") || path.startsWith("/api/inventory/")
                || path.startsWith("/api/perfumes/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el token JWT del encabezado "Authorization" si existe y comienza con "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {

                logger.warn("JWT inválido o expirado: " + e.getMessage());
            }
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            logger.info("Usuario autenticado: {}", userDetails.getUsername());
            logger.info("Roles asignados por JwtAuthFilter: {}", userDetails.getAuthorities());
            // Valida el token JWT
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // Crea un objeto de autenticación y lo establece en el SecurityContext
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

// Continúa con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}
