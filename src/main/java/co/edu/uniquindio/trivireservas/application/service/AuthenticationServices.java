package co.edu.uniquindio.trivireservas.application.service;

import co.edu.uniquindio.trivireservas.application.dto.EmailDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.*;
import co.edu.uniquindio.trivireservas.application.exception.ConflictValueException;
import co.edu.uniquindio.trivireservas.application.exception.PasswordCodeIncorrectException;
import co.edu.uniquindio.trivireservas.application.mapper.AbstractUserMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.AuthenticationUseCases;
import co.edu.uniquindio.trivireservas.application.ports.in.EmailUseCase;
import co.edu.uniquindio.trivireservas.application.ports.out.AbstractUserRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.ports.out.PasswordResetCodeRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.security.JWTUtils;
import co.edu.uniquindio.trivireservas.domain.AbstractUser;
import co.edu.uniquindio.trivireservas.domain.Host;
import co.edu.uniquindio.trivireservas.domain.User;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j // Herramienta de Lombok para Logging
@Service
@RequiredArgsConstructor
public class AuthenticationServices implements AuthenticationUseCases {

    private final AbstractUserRepositoryUseCases abstractUserRepositoryUseCases;

    private final PasswordResetCodeRepositoryUseCases passwordResetCodeRepositoryUseCases;

    private final EmailUseCase emailUseCase;

    private final JWTUtils jwtUtils;

    private final AbstractUserMapper abstractUserMapper;

    private final PasswordEncoder encoder;

    @Override
    public UUID getUUIDAuthenticatedUser() {
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal();

        log.info("Authenticated user: {}", user.getUsername());

        return UUID.fromString(user.getUsername());
    }

    @Override
    public Void register(RegisterDTO dto) {

        if (abstractUserRepositoryUseCases.doesEmailExist(dto.email())) {
            throw new ConflictValueException("Ya existe un usuario registrado con este email.");
        }

        if (!dto.phone().isBlank()) {
            if (abstractUserRepositoryUseCases.doesPhoneExist(dto.phone())) {
                throw new ConflictValueException("Ya existe un usuario registrado con este teléfono.");
            }
        }

        AbstractUserEntity newAbstractUser = abstractUserMapper.createAbstractUserEntity(dto);
        newAbstractUser.setPassword(encoder.encode(dto.password()));
        return abstractUserRepositoryUseCases.createUser(newAbstractUser);
    }

    @Override
    public TokenDTO hostLogin(LoginDTO dto, String mode) {

        // Inicialización de un Host.
        Host host = null;

        // Según el modo de inicio de sesión se busca el Host en el repositorio.
        if(mode.equals("email")) {

            if(dto.email() == null) throw new ConflictValueException("No se encontró el email.");

            if(dto.email().isBlank()) throw new ConflictValueException("No se encontró el email.");

            try {
                host = abstractUserRepositoryUseCases.getHostByEmail(dto.email()) ;
            } catch (EntityNotFoundException e) {
                log.info("Host with email {} not found", dto.email());
            }

        } else if (mode.equals("phone")) {

            if(dto.phone() == null) throw new ConflictValueException("No se encontró el teléfono.");

            if(dto.phone().isBlank()) throw new ConflictValueException("No se encontró el teléfono.");

            try {
                host = abstractUserRepositoryUseCases.getHostByPhone(dto.phone());
            } catch (EntityNotFoundException e) {
                log.info("Host with phone {} not found", dto.phone());
            }

        } else {
            throw new ConflictValueException("Error al iniciar sesión.");
        }

        // Se verifica que el host exista y que la contraseña sea correcta.
        if(host == null) {
            throw new ConflictValueException("No se encontró al host.");
        }
        
        if(!encoder.matches(dto.password(), host.getPassword())) {
            throw new ConflictValueException("La contraseña no es correcta.");
        }

        // Se genera el token JWT.
        String token = jwtUtils.generateToken(host.getUuid().toString(), createClaims(host));

        return new TokenDTO(token);
    }

    @Override
    public TokenDTO userLogin(LoginDTO dto, String mode) {

        // Inicialización de un User.
        User user = null;

        // Según el modo de inicio de sesión se busca el Host en el repositorio.
        if(mode.equals("email")) {

            if(dto.email().isBlank()) {
                throw new ConflictValueException("No se encontró el email.");
            }

            try {
                user = abstractUserRepositoryUseCases.getUserByEmail(dto.email()) ;
            } catch (EntityNotFoundException e) {
                log.info("User with email {} not found", dto.email());
            }

        } else if (mode.equals("phone")) {

            if(dto.phone().isBlank()) {
                throw new ConflictValueException("No se encontró el teléfono.");
            }

            try {
                user = abstractUserRepositoryUseCases.getUserByPhone(dto.phone());
            } catch (EntityNotFoundException e) {
                log.info("User with phone {} not found", dto.phone());
            }

        } else {
            throw new ConflictValueException("Error al iniciar sesión.");
        }

        // Se verifica que el usuario exista y que la contraseña sea correcta.
        if(user == null) {
            throw new ConflictValueException("No se encontró al usuario.");
        }
        
        if(!encoder.matches(dto.password(), user.getPassword())) {
            throw new ConflictValueException("La contraseña no es correcta.");
        }

        // Se genera el token JWT.
        String token = jwtUtils.generateToken(user.getUuid().toString(), createClaims(user));

        return new TokenDTO(token);
    }


    @Override
    public Void restPasswordRequest(ResetPasswordRequestDTO dto) {

        String code = generateResetCode(dto.email());

        EmailDTO emailDTO = new EmailDTO("Cambio de Contraseña de TriviReservas", """
                Buen día.
                Introduzca el siguiente código en la página web de TriviReservas para iniciar sesión.
                Atentamente: Equipo de TriviReservas.
                
                Código:
                """ + " " + code, dto.email());
        try {

            emailUseCase.sendMail(emailDTO);

        } catch (Exception e) {

            throw new ConflictValueException("No se pudo enviar el correo.");

        }

        return null;
    }

    @Override
    public Void resetPassword(UpdatePasswordWithCodeDTO dto) {

        if (passwordResetCodeRepositoryUseCases.validateCode(dto.email(), dto.code())) {

            log.info("Password code with email: {} and code: {} successfully validated.", dto.email(), dto.code());

            AbstractUser abstractUser = abstractUserRepositoryUseCases.getAbstractUserByEmail(dto.email());

            abstractUserRepositoryUseCases.updatePassword(abstractUser.getUuid(), encoder.encode(dto.newPassword()));

        } else {

            log.info("Password code with email: {} and code: {} was invalid.", dto.email(), dto.code());

            throw new PasswordCodeIncorrectException("El código expiró o no es válido.");
        }

        return null;
    }

    private Map<String, String> createClaims(AbstractUser abstractUser) {
        return Map.of(
                "email", abstractUser.getEmail(),
                "phone", abstractUser.getPhone(),
                "name", abstractUser.getName(),
                "role", abstractUser.getRole().name()
        );
    }

    private String generateResetCode(String email) {

        // Genera un código aleatorio de 6 dígitos (De 100001 hasta 999999) y lo guarda en la base de datos
        String code = (String.valueOf((int) (Math.random() * (999999 - 100000 + 1) + 100000)));
        passwordResetCodeRepositoryUseCases.createCode(email, code);

        return code;
    }
}
