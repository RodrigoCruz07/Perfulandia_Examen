package cl.duoc.rodrcruz.usersellermodule.security;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Este método se ejecutara cuando un usuario no autenticado intenta acceder a un recurso protegido
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: No autorizado. " + authException.getMessage());
    }
}
