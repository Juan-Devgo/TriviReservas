package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.UserDTO;

import java.util.List;

public interface GetUsersUseCase {

    List<UserDTO> getUsers();
}
