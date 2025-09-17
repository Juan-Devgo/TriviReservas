package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.LodgingDTO;
import co.edu.uniquindio.trivireservas.application.dto.UpdatePasswordDTO;
import co.edu.uniquindio.trivireservas.application.dto.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.application.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UsersUseCases {

    List<UserDTO> getUsers();

    UserDTO getUser(UUID id);

    List<LodgingDTO> getUserFavoriteLodgings(UUID userUUID);

    List<LodgingDTO> getUserRecommendationsLodgings(UUID userUUID);

    Void updateUser(UUID userUUID, UpdateUserDTO dto);

    Void updatePasswordUser(UUID userUUID, UpdatePasswordDTO dto);
}
