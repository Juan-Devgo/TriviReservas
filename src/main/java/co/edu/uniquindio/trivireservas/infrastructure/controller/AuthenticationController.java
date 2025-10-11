package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.*;
import co.edu.uniquindio.trivireservas.application.dto.user.*;
import co.edu.uniquindio.trivireservas.application.ports.in.AuthenticationUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationUseCases authenticationUseCases;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<Void>> registerUser(@Valid @RequestBody RegisterDTO dto) {
        return ResponseEntity.status(201).body(new ResponseDTO<>(false,
                "Usuario creado satisfactoriamente.", authenticationUseCases.register(dto)));
    }

    @PostMapping("/user/login/{mode}")
    public ResponseEntity<ResponseDTO<TokenDTO>> loginUser(@PathVariable String mode, @Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Autenticación exitosa, token JWT generado.", authenticationUseCases.userLogin(dto, mode)));
    }

    @PostMapping("/host/login/{mode}")
    public ResponseEntity<ResponseDTO<TokenDTO>> loginHost(@PathVariable String mode, @Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Autenticación exitosa, token JWT generado.", authenticationUseCases.hostLogin(dto, mode)));
    }

    @PostMapping("/reset_password")
    public ResponseEntity<ResponseDTO<Void>> resetPasswordRequest(@Valid @RequestBody ResetPasswordRequestDTO email) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Solicitud de restablecimiento de contraseña enviada satisfactoriamente.",
                authenticationUseCases.restPasswordRequest(email)));
    }

    @PatchMapping("/reset_password")
    public ResponseEntity<ResponseDTO<Void>> resetPassword(@Valid @RequestBody UpdatePasswordDTO code) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Contraseña restablecida satisfactoriamente.", authenticationUseCases.resetPassword(code)));
    }
}
