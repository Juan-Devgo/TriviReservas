package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.LoginDTO;
import co.edu.uniquindio.trivireservas.application.dto.RegisterDTO;
import co.edu.uniquindio.trivireservas.application.dto.ResetPasswordDTO;

public interface AuthenticationUseCases {

    void hostRegister(RegisterDTO dto);

    void hostLogin(LoginDTO dto);

    void userRegister(RegisterDTO dto);

    void userLogin(LoginDTO dto);

    void restPassword(ResetPasswordDTO dto);

}
