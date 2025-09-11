package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import co.edu.uniquindio.trivireservas.application.dto.UserDTO;
import co.edu.uniquindio.trivireservas.application.ports.in.UsersUseCases;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UsersUseCases usersUseCases;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getUsers(){
        return ResponseEntity.ok(new ResponseDTO<>(false, usersUseCases.getUsers()));
    }


}
