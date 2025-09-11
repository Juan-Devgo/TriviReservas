package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import co.edu.uniquindio.trivireservas.application.dto.UserDTO;
import co.edu.uniquindio.trivireservas.application.ports.in.GetUsersUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private GetUsersUseCase getUsersUseCase;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getUsers(){
        return ResponseEntity.ok(new ResponseDTO<>(false, getUsersUseCase.getUsers()));
    }


}
