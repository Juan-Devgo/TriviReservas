package co.edu.uniquindio.trivireservas.ServiceTest;

import co.edu.uniquindio.trivireservas.application.dto.EmailDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.*;
import co.edu.uniquindio.trivireservas.application.mapper.UserMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.EmailUseCase;
import co.edu.uniquindio.trivireservas.application.security.JWTUtils;
import co.edu.uniquindio.trivireservas.application.service.AuthenticationServices;
import co.edu.uniquindio.trivireservas.application.service.SMTPProperties;
import co.edu.uniquindio.trivireservas.application.ports.out.AbstractUserRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.ports.out.PasswordResetCodeRepositoryUseCases;
import co.edu.uniquindio.trivireservas.domain.Host;
import co.edu.uniquindio.trivireservas.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private AbstractUserRepositoryUseCases userRepository;

    @Mock
    private PasswordResetCodeRepositoryUseCases passwordResetRepo;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailUseCase emailUseCase;

    @Mock
    private JWTUtils jwtUtils;

    @InjectMocks
    private AuthenticationServices authenticationServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_WithNonDuplicatedEmail_ShouldCallCreateUser() {
        RegisterDTO dto = new RegisterDTO("Alice", "alice@test.com", "123456", "1234567890","0000000000","2010,4,3");

        when(userRepository.getUserByEmail(dto.email())).thenReturn(null);
        when(userRepository.getHostByEmail(dto.email())).thenReturn(null);

        authenticationServices.register(dto);

        verify(userMapper).fromRegisterDTO(dto);
        verify(userRepository).createUser(any());
    }

    @Test
    void hostLogin_WithValidEmailAndPassword_ShouldReturnTokenDTO() {
        String email = "host@test.com";
        String password = "password123";
        Host host = mock(Host.class);
        UUID uuid = UUID.randomUUID();

        when(host.getPassword()).thenReturn("hashed");
        when(host.getUuid()).thenReturn(uuid);
        when(userRepository.getHostByEmail(email)).thenReturn(host);
        when(passwordEncoder.matches(password, "hashed")).thenReturn(true);
        when(jwtUtils.generateToken(eq(uuid.toString()), anyMap())).thenReturn("token123");

        LoginDTO dto = new LoginDTO(email, null, password);
        TokenDTO token = authenticationServices.hostLogin(dto, "email");

        assertEquals("token123", token.token());
    }

    @Test
    void userLogin_WithInvalidPassword_ShouldThrowException() {
        String email = "user@test.com";
        String password = "wrongpassword";
        User user = mock(User.class);

        when(user.getPassword()).thenReturn("hashed");
        when(userRepository.getUserByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, "hashed")).thenReturn(false);

        LoginDTO dto = new LoginDTO(email, null, password);

        assertThrows(RuntimeException.class, () -> authenticationServices.userLogin(dto, "email"));
    }

    @Test
    void restPasswordRequest_ShouldGenerateCodeAndSendEmail() throws Exception {
        String email = "user@test.com";
        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO(email);

        // Mock de repositorio de códigos para no fallar
        doNothing().when(passwordResetRepo).createCode(anyString(), anyString());
        when(passwordResetRepo.validateCode(anyString(), anyString())).thenReturn(true);
        doNothing().when(emailUseCase).sendMail(any(EmailDTO.class));

        assertDoesNotThrow(() -> authenticationServices.restPasswordRequest(dto));
        verify(emailUseCase).sendMail(any(EmailDTO.class));
    }
}
