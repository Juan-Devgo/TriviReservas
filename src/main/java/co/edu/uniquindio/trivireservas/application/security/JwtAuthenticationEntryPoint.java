package co.edu.uniquindio.trivireservas.application.security;

import co.edu.uniquindio.trivireservas.application.dto.ErrorDTO;
import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws
                         IOException {

        ResponseDTO<ErrorDTO> dto = new ResponseDTO<>(true, "No Autorizado", new ErrorDTO("403", authException.getMessage()));
        response.setContentType("application/json");
        response.setStatus(403);
        response.getWriter().write(new ObjectMapper().writeValueAsString(dto));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
