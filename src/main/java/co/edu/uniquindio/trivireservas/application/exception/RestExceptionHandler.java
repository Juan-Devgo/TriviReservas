package co.edu.uniquindio.trivireservas.application.exception;

import co.edu.uniquindio.trivireservas.application.dto.ErrorDTO;
import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<ErrorDTO>> handleRuntimeException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(new ResponseDTO<>(true , "", new ErrorDTO("n/n", ex.getMessage())));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO<ErrorDTO>> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(new ResponseDTO<>(true , "", new ErrorDTO("n/n", ex.getMessage())));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseDTO<ErrorDTO>> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity.status(401).body(new ResponseDTO<>(true , "", new ErrorDTO("401", "Acceso denegado, no has iniciado sesión.")));
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ResponseDTO<ErrorDTO>> handleLoginException(LoginException ex) {
        return ResponseEntity.status(400).body(new ResponseDTO<>(true , "", new ErrorDTO("RYA01-1", "Error en la solicitud al tratar de iniciar sesión.")));
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ResponseDTO<ErrorDTO>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(403).body(new ResponseDTO<>(true , "", new ErrorDTO("403", "El usuario no tiene permisos para realizar esta acción. " + ex.getMessage())));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO<ErrorDTO>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(404).body(new ResponseDTO<>(true , "", new ErrorDTO("RYA02-1", "No se ha encontrado la entidad con el id: " + ex.getMessage())));
    }

    @ExceptionHandler(ConflictValueException.class)
    public ResponseEntity<ResponseDTO<ErrorDTO>> handleConflictValueException(ConflictValueException ex) {
        return ResponseEntity.status(409).body(new ResponseDTO<>(true , "", new ErrorDTO("RYA02-1", "Error en la solicitud por valor en conflicto.")));
    }
}
