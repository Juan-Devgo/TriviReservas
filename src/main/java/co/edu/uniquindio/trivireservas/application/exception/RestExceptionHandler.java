package co.edu.uniquindio.trivireservas.application.exception;

import co.edu.uniquindio.trivireservas.application.dto.ErrorDTO;
import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler { // TODO

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ResponseDTO<ErrorDTO>> handleLoginException(LoginException ex) {
        return ResponseEntity.status(400).body(new ResponseDTO<>(true , "", new ErrorDTO("RYA01-1", "Error en la solicitud al tratar de iniciar sesión.")));
    }

    @ExceptionHandler(ConflictValueException.class)
    public ResponseEntity<ResponseDTO<ErrorDTO>> handleConflictValueException(ConflictValueException ex) {
        return ResponseEntity.status(409).body(new ResponseDTO<>(true , "", new ErrorDTO("RYA02-1", "Error en la solicitud por valor en conflicto.")));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO<ErrorDTO>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(404).body(new ResponseDTO<>(true , "", new ErrorDTO("RYA02-1", "No se ha encontrado la entidad con el id: " + ex.getMessage())));
    }
}
