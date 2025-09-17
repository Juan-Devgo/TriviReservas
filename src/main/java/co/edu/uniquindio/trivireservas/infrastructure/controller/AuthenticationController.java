package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.*;
import co.edu.uniquindio.trivireservas.application.ports.in.AuthenticationUseCases;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private AuthenticationUseCases authenticationUseCases;

    @PostMapping("/user/register")
    public ResponseEntity<ResponseDTO<Void>> registerUser(@Valid @RequestBody UserDTO dto) {
        return ResponseEntity.status(201).body(new ResponseDTO<>(false,
                "Usuario creado satisfactoriamente.", authenticationUseCases.userRegister(dto)));
    }

    @PostMapping("/user/login/{mode}")
    public ResponseEntity<ResponseDTO<Void>> loginUser(@PathVariable String mode, @Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Autenticación exitosa, token JWT generado.", authenticationUseCases.userLogin(dto, mode)));
    }

    @PostMapping("/host/register")
    public ResponseEntity<ResponseDTO<Void>> registerHost(@RequestBody UserDTO dto) {
        return ResponseEntity.status(201).body(new ResponseDTO<>(false,
                "Anfitrión creado satisfactoriamente.", authenticationUseCases.hostRegister(dto)));
    }

    @PostMapping("/host/login/{mode}")
    public ResponseEntity<ResponseDTO<Void>> loginHost(@PathVariable String mode, @Valid @RequestBody LoginDTO dto) {
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
