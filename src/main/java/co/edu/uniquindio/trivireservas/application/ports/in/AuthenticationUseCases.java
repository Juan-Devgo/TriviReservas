package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.LoginDTO;
import co.edu.uniquindio.trivireservas.application.dto.ResetPasswordDTO;
import co.edu.uniquindio.trivireservas.application.dto.UserDTO;

public interface AuthenticationUseCases {

    void hostRegister(UserDTO dto);

    void hostLogin(LoginDTO dto);

    void userRegister(UserDTO dto);

    void userLogin(LoginDTO dto);

    void restPassword(ResetPasswordDTO dto);
}
