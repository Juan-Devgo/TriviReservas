package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import co.edu.uniquindio.trivireservas.application.ports.in.ImageUseCases;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/image")
public class ImageController {

    private ImageUseCases imageUseCases;

    @PostMapping("/")
    public ResponseEntity<ResponseDTO<Void>> uploadImage(){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Imagen posteada en la nube correctamente",
                imageUseCases.saveImage("img"))); // -> TODO
    }

    @DeleteMapping("/")
    public ResponseEntity<ResponseDTO<Void>> deleteImage(){
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Imagen eliminada de la nube correctamente",
                imageUseCases.deleteImage("img"))); // -> TODO
    }
}
