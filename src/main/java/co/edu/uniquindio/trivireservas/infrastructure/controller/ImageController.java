package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import co.edu.uniquindio.trivireservas.application.ports.in.ImagesUseCases;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    private final ImagesUseCases imagesUseCases;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Map>> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("Request received to upload image file {}", file.getOriginalFilename());
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Imagen posteada en la nube correctamente",
                imagesUseCases.saveImage(file)));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Map>> deleteImage(@RequestParam("id") String id) throws Exception {
        log.info("Request received to delete image {}", id);
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Imagen eliminada de la nube correctamente",
                imagesUseCases.deleteImage(id)));
    }
}
