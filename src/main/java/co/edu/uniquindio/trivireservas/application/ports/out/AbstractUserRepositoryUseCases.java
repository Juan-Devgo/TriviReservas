package co.edu.uniquindio.trivireservas.application.ports.out;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdatePasswordDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.domain.AbstractUser;
import co.edu.uniquindio.trivireservas.domain.Host;
import co.edu.uniquindio.trivireservas.domain.User;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;

import java.util.List;
import java.util.UUID;

public interface AbstractUserRepositoryUseCases {

    PageResponse<AbstractUser> getUsers(int page);

    User getUserByUUID(UUID uuid);

    User getUserByEmail(String email);

    User getUserByPhone(String phone);

    Host getHostByUUID(UUID uuid);

    Host getHostByEmail(String email);

    Host getHostByPhone(String phone);

    Void createUser(AbstractUserEntity user);

    Void updateUser(UUID userUUID, UpdateUserDTO dto);

    Void updatePassword(UUID userUUID, String newPassword);
}
