package co.edu.uniquindio.trivireservas.ServiceTest;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDTO;
import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDetailsDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdatePasswordDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;
import co.edu.uniquindio.trivireservas.application.mapper.LodgingMapper;
import co.edu.uniquindio.trivireservas.application.mapper.UserMapper;
import co.edu.uniquindio.trivireservas.application.ports.out.AbstractUserRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.ports.out.LodgingRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.service.UsersServices;
import co.edu.uniquindio.trivireservas.domain.Lodging;
import co.edu.uniquindio.trivireservas.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private AbstractUserRepositoryUseCases userRepository;

    @Mock
    private LodgingRepositoryUseCases lodgingRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private LodgingMapper lodgingMapper;

    @InjectMocks
    private UsersServices usersServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsers_WithExistingUsers_ShouldReturnAllAsDTOs() {
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        PageResponse<User> usersPage = new PageResponse<>(List.of(user1, user2), 0, 10, 1, true);
        when(userRepository.getUsers(0)).thenReturn(usersPage);

        UserDTO dto1 = new UserDTO("01","Carmelo", "USER", "mimi@gmail.com", "1234567890", "2000,24,12");
        UserDTO dto2 = new UserDTO("02","Carmela", "USER", "carmelita@gmail.com", "2324562266", "2001,31,10");

        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        PageResponse<UserDTO> result = usersServices.getUsers(0);

        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals("Carmelo", result.content().get(0).name());
        assertEquals("Carmela", result.content().get(1).name());
    }


    @Test
    void getUser_WithValidUUID_ShouldReturnMappedUserDTO() {
        UUID uuid = UUID.randomUUID();
        User user = mock(User.class);
        UserDTO expectedDto = new UserDTO(uuid.toString(), "Charlie", "meme@gmail.com", "1001001010", "3987645322", "1500,1,09");

        when(userRepository.getUserByUUID(uuid)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expectedDto);

        UserDTO result = usersServices.getUser(uuid);

        assertNotNull(result);
        assertEquals(expectedDto.uuid(), result.uuid());
        assertEquals(expectedDto.name(), result.name());
        assertEquals(expectedDto.email(), result.email());
        assertEquals(expectedDto.phone(), result.phone());
    }

    @Test
    void getUserFavoriteLodgings_WithValidUser_ShouldReturnListOfLodgings() {
        UUID userUUID = UUID.randomUUID();
        int page = 0;

        Lodging lodging = mock(Lodging.class);

        LodgingDetailsDTO details = new LodgingDetailsDTO(
                UUID.randomUUID().toString(),
                200.0,
                List.of(),
                List.of(),
                2,
                null
        );

        LodgingDTO lodgingDTO = new LodgingDTO(
                "Lodging 1",
                UUID.randomUUID().toString(),
                "wakawaka",
                "HUT",
                "ACTIVE",
                "1600,30,12",
                details
        );

        PageResponse<Lodging> favoriteLodgingsPage = new PageResponse<>(
                List.of(lodging),
                page,
                10,
                1,
                true
        );

        when(lodgingRepository.getFavoriteLodgingsByUserUUID(userUUID,page)).thenReturn(favoriteLodgingsPage);
        when(lodgingMapper.toDto(lodging)).thenReturn(lodgingDTO);

        PageResponse<LodgingDTO> result = usersServices.getUserFavoriteLodgings(userUUID, page);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals("Lodging 1", result.content().get(0).title());
        assertEquals(page, result.page());
        assertEquals(10, result.pageSize());
        assertEquals(1, result.totalPages());
        assertTrue(result.last());
    }

    @Test
    void getUserRecommendationsLodgings_WithValidUser_ShouldReturnListOfRecommendedLodgings() {
        UUID userUUID = UUID.randomUUID();
        Lodging lodging = mock(Lodging.class);

        LodgingDetailsDTO details = new LodgingDetailsDTO(
                UUID.randomUUID().toString(),
                250.0,
                List.of(),
                List.of(),
                3,
                null
        );

        LodgingDTO lodgingDTO = new LodgingDTO(
                "Lodging 2",
                UUID.randomUUID().toString(),
                "hola",
                "HUT",
                "ACTIVE",
                "2000,20,10",
                details
        );

        PageResponse<Lodging> recommendedPage = new PageResponse<>(List.of(lodging), 0, 10, 1, true);
        when(lodgingRepository.getRecommendedLodgingsByUserUUID(userUUID,0)).thenReturn(recommendedPage);
        when(lodgingMapper.toDto(lodging)).thenReturn(lodgingDTO);

        PageResponse<LodgingDTO> result = usersServices.getUserRecommendationsLodgings(userUUID,0);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals("Lodging 2", result.content().get(0).title());
    }

    @Test
    void updateUser_WithValidData_ShouldInvokeRepositoryUpdate() {
        UUID uuid = UUID.randomUUID();
        List<String> documentos = List.of("doc1.pdf", "doc2.pdf");
        UpdateUserDTO dto = new UpdateUserDTO("newName", "3005467896","una url x", "integral de coseno hiperbolico de x es seno hiperbolico de x mas c",documentos);

        usersServices.updateUser(uuid, dto);

        verify(userRepository).updateUser(uuid, dto);
    }

    @Test
    void updatePassword_WithValidData_ShouldInvokeRepositoryUpdatePassword() {
        UUID uuid = UUID.randomUUID();
        UpdatePasswordDTO dto = new UpdatePasswordDTO("old", "new");

        usersServices.updatePasswordUser(uuid, dto);

        verify(userRepository).updatePassword(uuid, dto);
    }

    @Test
    void encodePassword_WithValidString_ShouldGenerateValidHash() {
        String password = "es_un_secretooo";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String encoded = encoder.encode(password);

        assertTrue(encoder.matches(password, encoded));
    }
}
