package co.edu.uniquindio.trivireservas.application.service;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdatePasswordDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;
import co.edu.uniquindio.trivireservas.application.exception.ForbiddenActionException;
import co.edu.uniquindio.trivireservas.application.mapper.HostMapper;
import co.edu.uniquindio.trivireservas.application.mapper.LodgingMapper;
import co.edu.uniquindio.trivireservas.application.mapper.UserMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.UsersUseCases;
import co.edu.uniquindio.trivireservas.application.ports.out.AbstractUserRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.ports.out.LodgingRepositoryUseCases;
import co.edu.uniquindio.trivireservas.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServices implements UsersUseCases {

    private final AbstractUserRepositoryUseCases abstractUserRepositoryUseCases;

    private final LodgingRepositoryUseCases lodgingRepositoryUseCases;

    private final UserMapper userMapper;

    private final HostMapper hostMapper;

    private final LodgingMapper lodgingMapper;

    private final PasswordEncoder encoder;
    private final AuthenticationServices authenticationServices;

    @Override
    public PageResponse<UserDTO> getUsers(int page) {

        PageResponse<AbstractUser> usersPage = abstractUserRepositoryUseCases.getUsers(page);

        List<AbstractUser> abstractUsers = usersPage.content();
        List<User> users = abstractUsers
                .stream().filter(abs -> abs.getRole().equals(UserRole.USER))
                .map(u -> (User) u).toList();

        List<Host> hosts = abstractUsers
                .stream().filter(abs -> abs.getRole().equals(UserRole.HOST))
                .map(h -> (Host) h).toList();

        List<UserDTO> userDTOs = new ArrayList<>(userMapper.toDtoFromDomainList(users));
        userDTOs.addAll((hostMapper.toDtoFromDomainList(hosts)));

        return new PageResponse<>(
                userDTOs,
                page,
                10,
                usersPage.totalPages(),
                usersPage.hasNext()
        );
    }

    @Override
    public UserDTO getUser(UUID uuid) {
        return userMapper.toDtoFromDomain((User) abstractUserRepositoryUseCases.getUserByUUID(uuid));
    }

    @Override
    public PageResponse<LodgingDTO> getUserFavoriteLodgings(UUID userUUID, int page) {

        PageResponse<Lodging> lodgingsPage = lodgingRepositoryUseCases.getFavoriteLodgingsByUserUUID(userUUID, page);
        List<Lodging> lodgings = lodgingsPage.content();

        List<LodgingDTO> lodgingDTOs = lodgingMapper.toDtoFromDomainList(lodgings);

        return new PageResponse<>(
                lodgingDTOs,
                page,
                10,
                lodgingsPage.totalPages(),
                lodgingsPage.hasNext()
        );
    }

    @Override
    public PageResponse<LodgingDTO> getUserRecommendationsLodgings(UUID userUUID, int page) {
        PageResponse<Lodging> recommendedPage = lodgingRepositoryUseCases.getRecommendedLodgingsByUserUUID(userUUID, page);

        List<Lodging> lodgings = recommendedPage.content();
        List<LodgingDTO> recommendedDTOs = lodgingMapper.toDtoFromDomainList(lodgings);

        return new PageResponse<>(
                recommendedDTOs,
                page,
                recommendedPage.pageSize(),
                recommendedPage.totalPages(),
                recommendedPage.hasNext()
        );
    }


    @Override
    public Void updateUser(UUID userUUID, UpdateUserDTO dto) {

        // Se comprueba que el usuario autenticado es el mismo que el que se quiere actualizar

        if(!authenticationServices.getUUIDAuthenticatedUser().equals(userUUID)) {
            throw new ForbiddenActionException("El usuario autenticado no es el mismo que se quiere actualizar.");
        }

        return abstractUserRepositoryUseCases.updateUser(userUUID, dto);
    }

    @Override
    public Void updatePasswordUser(UUID userUUID, UpdatePasswordDTO dto) {

        // Se comprueba que el usuario autenticado es el mismo que el que se quiere actualizar

        UUID authenticatedUserUUID = authenticationServices.getUUIDAuthenticatedUser();

        if(!authenticatedUserUUID.equals(userUUID)) {
            log.warn("The authenticated user with UUID {} tried to update the password of another user", authenticatedUserUUID);
            throw new ForbiddenActionException("El usuario autenticado no es el mismo que se quiere actualizar.");
        }

        return abstractUserRepositoryUseCases.updatePassword(userUUID, encoder.encode(dto.codeOrPassword()));
    }
}
