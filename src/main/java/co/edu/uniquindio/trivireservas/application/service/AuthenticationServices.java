package co.edu.uniquindio.trivireservas.application.service;

import co.edu.uniquindio.trivireservas.application.dto.EmailDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.*;
import co.edu.uniquindio.trivireservas.application.mapper.UserMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.AuthenticationUseCases;
import co.edu.uniquindio.trivireservas.application.ports.in.EmailUseCase;
import co.edu.uniquindio.trivireservas.application.ports.out.AbstractUserRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.ports.out.PasswordResetCodeRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.security.JWTUtils;
import co.edu.uniquindio.trivireservas.domain.AbstractUser;
import co.edu.uniquindio.trivireservas.domain.Host;
import co.edu.uniquindio.trivireservas.domain.User;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServices implements AuthenticationUseCases {

    private final AbstractUserRepositoryUseCases abstractUserRepositoryUseCases;

    private final PasswordResetCodeRepositoryUseCases passwordResetCodeRepositoryUseCases;

    private final EmailUseCase emailUseCase;

    private final JWTUtils jwtUtils;

    private final UserMapper userMapper;

    private final PasswordEncoder encoder;

    @Override
    public UUID getUUIDAuthenticatedUser() {
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal();

        return UUID.fromString(user.getUsername());
    }

    @Override
    public Void register(RegisterDTO dto) {

        if(isEmailDuplicated(dto.email())) {
            throw new RuntimeException("User already registered"); // TODO Asignar excepción
        }

        if(!dto.phone().isBlank()) {
            if (isPhoneDuplicated(dto.phone())) {
                throw new RuntimeException("User already registered"); // TODO Asignar excepción
            }
        }

        AbstractUserEntity newAbstractUser = userMapper.createAbstractUserEntity(dto);
        newAbstractUser.setPassword(encoder.encode(dto.password()));
        return abstractUserRepositoryUseCases.createUser(newAbstractUser);
    }

    @Override
    public TokenDTO hostLogin(LoginDTO dto, String mode) {

        // Inicialización de un Host.
        Host host;

        // Según el modo de inicio de sesión se busca el Host en el repositorio.
        if(mode.equals("email")) {

            if(dto.email().isBlank()) {
                throw new RuntimeException("Invalid email"); // TODO Asignar excepción
            }

            host = abstractUserRepositoryUseCases.getHostByEmail(dto.email()) ;

        } else if (mode.equals("phone")) {

            if(dto.phone().isBlank()) {
                throw new RuntimeException("Invalid phone"); // TODO Asignar excepción
            }

            host = abstractUserRepositoryUseCases.getHostByPhone(dto.phone());

        } else {
            throw new RuntimeException("Invalid mode"); //TODO Asignar excepción
        }

        // Se verifica que el usuario sí exista.
        if(host == null) {
            throw new RuntimeException("Host not found"); //TODO Asignar excepción
        }

        // Se verifica que sí sea la contraseña correcta.
        if(!encoder.matches(dto.password(), host.getPassword())) {
            throw new RuntimeException("Invalid password"); //TODO Asignar excepción
        }

        // Se genera el token JWT.
        String token = jwtUtils.generateToken(host.getUuid().toString(), createClaims(host));

        return new TokenDTO(token);
    }

    @Override
    public TokenDTO userLogin(LoginDTO dto, String mode) {

        // Inicialización de un User.
        User user;

        // Según el modo de inicio de sesión se busca el Host en el repositorio.
        if(mode.equals("email")) {

            if(dto.email().isBlank()) {
                throw new RuntimeException("Invalid email"); // TODO Asignar excepción
            }

            user = abstractUserRepositoryUseCases.getUserByEmail(dto.email()) ;

        } else if (mode.equals("phone")) {

            if(dto.phone().isBlank()) {
                throw new RuntimeException("Invalid phone"); // TODO Asignar excepción
            }

            user = abstractUserRepositoryUseCases.getUserByPhone(dto.phone());

        } else {
            throw new RuntimeException("Invalid mode"); //TODO Asignar excepción
        }

        // Se verifica que el usuario sí exista.
        if(user == null) {
            throw new RuntimeException("User not found"); //TODO Asignar excepción
        }

        // Se verifica que sí sea la contraseña correcta.
        if(!encoder.matches(dto.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password"); //TODO Asignar excepción
        }

        // Se genera el token JWT.
        String token = jwtUtils.generateToken(user.getUuid().toString(), createClaims(user));

        return new TokenDTO(token);
    }


    @Override
    public Void restPasswordRequest(ResetPasswordRequestDTO dto) {

        String code;

        try {

            code = generateResetCode(dto.email());

        } catch (Exception e) {

            throw new RuntimeException(e); // TODO Asignar excepción

        }

        EmailDTO emailDTO = new EmailDTO("Cambio de Contraseña de TriviReservas", """
                Buen día.
                Introduzca el siguiente código en la página web de TriviReservas para iniciar sesión.
                Atentamente: Equipo de TriviReservas.
                
                Código:
                """ + " " + code, dto.email());
        try {

            emailUseCase.sendMail(emailDTO);

        } catch (Exception e) {

            throw new RuntimeException("No se ha podido enviar el email: " + e); // TODO Asignar excepción

        }

        return null;
    }

    @Override
    public Void resetPassword(UpdatePasswordDTO dto) {
        return null;
    }

    private Map<String, String> createClaims(AbstractUser abstractUser) {
        return Map.of(
                "email", abstractUser.getEmail(),
                "phone", abstractUser.getPhone(),
                "name", abstractUser.getName(),
                "role", "ROLE_" + abstractUser.getRole().name()
        );
    }

    private boolean isEmailDuplicated(String email) {

        // Verifica si existe un usuario o host con el email
        return abstractUserRepositoryUseCases.getUserByEmail(email) != null || abstractUserRepositoryUseCases.getHostByEmail(email) != null;
    }

    private boolean isPhoneDuplicated(String phone) {

        // Verifica si existe un usuario o host con el teléfono
        return abstractUserRepositoryUseCases.getUserByPhone(phone) != null || abstractUserRepositoryUseCases.getHostByPhone(phone) != null;
    }

    private String generateResetCode(String email) throws Exception {

        // Genera un código aleatorio de 6 dígitos (De 100001 hasta 999999) y lo guarda en la base de datos
        String code = (String.valueOf((int) (Math.random() * (999999 - 100000 + 1) + 100000)));
        passwordResetCodeRepositoryUseCases.createCode(email, code);

        if(!passwordResetCodeRepositoryUseCases.validateCode(email, code)) { // TODO preguntar si es necesario
            throw new Exception("Error generating code");
        }

        return code;
    }
}
