package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.EmailDTO;

public interface EmailUseCase {

    void sendMail(EmailDTO emailDTO) throws Exception;
}
