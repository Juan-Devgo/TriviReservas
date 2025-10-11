package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.user.*;

import java.util.UUID;

public interface AuthenticationUseCases {

    // Métodos de aceso al UUID de usuarios autenticados

    UUID getUUIDAuthenticatedUser();

    // Métodos de la API

    Void register(RegisterDTO dto);

    TokenDTO hostLogin(LoginDTO dto, String mode);

    TokenDTO userLogin(LoginDTO dto, String mode);

    Void restPasswordRequest(ResetPasswordRequestDTO dto);

    Void resetPassword(UpdatePasswordDTO dto);
}
