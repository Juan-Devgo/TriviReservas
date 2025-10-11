package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdatePasswordDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;

import java.util.UUID;

public interface UsersUseCases {

    PageResponse<UserDTO> getUsers(int page);

    UserDTO getUser(UUID id);

    PageResponse<LodgingDTO> getUserFavoriteLodgings(UUID userUUID, int page);

    PageResponse<LodgingDTO> getUserRecommendationsLodgings(UUID userUUID, int page);

    Void updateUser(UUID userUUID, UpdateUserDTO dto);

    Void updatePasswordUser(UUID userUUID, UpdatePasswordDTO dto);
}
