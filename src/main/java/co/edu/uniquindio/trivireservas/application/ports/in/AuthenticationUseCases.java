package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.LoginDTO;
import co.edu.uniquindio.trivireservas.application.dto.ResetPasswordRequestDTO;
import co.edu.uniquindio.trivireservas.application.dto.UpdatePasswordDTO;
import co.edu.uniquindio.trivireservas.application.dto.UserDTO;

public interface AuthenticationUseCases {

    Void hostRegister(UserDTO dto);

    Void hostLogin(LoginDTO dto, String mode);

    Void userRegister(UserDTO dto);

    Void userLogin(LoginDTO dto, String mode);

    Void restPasswordRequest(ResetPasswordRequestDTO dto);

    Void resetPassword(UpdatePasswordDTO dto);
}
