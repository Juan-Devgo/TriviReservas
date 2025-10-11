package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.*;
import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdatePasswordDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;
import co.edu.uniquindio.trivireservas.application.ports.in.UsersUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UsersUseCases usersUseCases;

    @GetMapping
    public ResponseEntity<ResponseDTO<PageResponse<UserDTO>>> getUsers(@RequestParam int page){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Usuarios obtenidos satisfactoriamente.", usersUseCases.getUsers(page)));
    }

    @GetMapping("/{userUUID}")
    public ResponseEntity<ResponseDTO<UserDTO>> getUser(@PathVariable String userUUID){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Usuario obtenido satisfactoriamente.", usersUseCases.getUser(UUID.fromString(userUUID))));
    }

    @GetMapping("/{userUUID}/favorites")
    public ResponseEntity<ResponseDTO<PageResponse<LodgingDTO>>> getUserFavoriteLodgings(
            @PathVariable String userUUID,
            @RequestParam int page
    ){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Alojamientos favoritos obtenidos satisfactoriamente.",
                usersUseCases.getUserFavoriteLodgings(UUID.fromString(userUUID), page)));
    }

    @GetMapping("/{userUUID}/recommendations")
    public ResponseEntity<ResponseDTO<PageResponse<LodgingDTO>>> getUserRecommendationsLodgings(
            @PathVariable String userUUID,
            @RequestParam int page
    ){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Alojamientos recomendados obtenidos satisfactoriamente.",
                usersUseCases.getUserRecommendationsLodgings(UUID.fromString(userUUID), page)));
    }

    @PatchMapping("/{userUUID}")
    public ResponseEntity<ResponseDTO<Void>> updateUser(
            @Valid @RequestBody UpdateUserDTO dto,
            @PathVariable String userUUID
    ){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Usuario actualizado satisfactoriamente.",
                usersUseCases.updateUser(UUID.fromString(userUUID), dto)));
    }

    @PatchMapping("/{userUUID}/password")
    public ResponseEntity<ResponseDTO<Void>> updatePasswordUser(
            @PathVariable String userUUID,
            @Valid @RequestBody UpdatePasswordDTO dto
    ){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Contraseña restablecida satisfactoriamente.",
                usersUseCases.updatePasswordUser(UUID.fromString(userUUID), dto)));
    }
}
