package co.edu.uniquindio.trivireservas.application.exception;

import co.edu.uniquindio.trivireservas.application.dto.ErrorDTO;
import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ResponseDTO<ErrorDTO>> handleLoginException(LoginException ex) {
        return ResponseEntity.status(400).body(new ResponseDTO<>(true , new ErrorDTO("RYA01-1", "Error en la solitud al tratar de iniciar sesión.")));
    }
}
