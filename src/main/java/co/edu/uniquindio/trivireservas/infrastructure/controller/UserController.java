package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.*;
import co.edu.uniquindio.trivireservas.application.ports.in.UsersUseCases;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UsersUseCases usersUseCases;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getUsers(){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Usuarios obtenidos satisfactoriamente.", usersUseCases.getUsers()));
    }

    @GetMapping("/{userUUID}")
    public ResponseEntity<ResponseDTO<UserDTO>> getUser(@PathVariable String userUUID){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Usuario obtenido satisfactoriamente.", usersUseCases.getUser(UUID.fromString(userUUID))));
    }

    @GetMapping("/{userUUID}/favorites")
    public ResponseEntity<ResponseDTO<List<LodgingDTO>>> getUserFavoriteLodgings(@PathVariable String userUUID){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Alojamientos favoritos obtenidos satisfactoriamente.",
                usersUseCases.getUserFavoriteLodgings(UUID.fromString(userUUID))));
    }

    @GetMapping("/{userUUID}/recommendations")
    public ResponseEntity<ResponseDTO<List<LodgingDTO>>> getUserRecommendationsLodgings(@PathVariable String userUUID){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Alojamientos recomendados obtenidos satisfactoriamente.",
                usersUseCases.getUserRecommendationsLodgings(UUID.fromString(userUUID))));
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
