package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.CommentDTO;
import co.edu.uniquindio.trivireservas.application.dto.LodgingDTO;
import co.edu.uniquindio.trivireservas.application.dto.LodgingMetricsDTO;
import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import co.edu.uniquindio.trivireservas.application.ports.in.LodgingFilters;
import co.edu.uniquindio.trivireservas.application.ports.in.LodgingsUseCases;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lodgings")
public class LodgingController {

    private LodgingsUseCases lodgingsUseCases;

    @GetMapping("/")
    public ResponseEntity<ResponseDTO<List<LodgingDTO>>> getLodgings(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String checkIn,
            @RequestParam(required = false) String checkOut
    ) {
        LodgingFilters filters = new LodgingFilters(city, minPrice, maxPrice, checkIn, checkOut);
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Alojamientos obtenidos satisfactoriamente.", lodgingsUseCases.getLodgings(filters)));
    }

    @GetMapping("/{lodgingUUID}")
    public ResponseEntity<ResponseDTO<LodgingDTO>> getLodging(@PathVariable String lodgingUUID) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Alojamiento obtenido satisfactoriamente.",
                lodgingsUseCases.getLodging(UUID.fromString(lodgingUUID))));
    }

    @GetMapping("/{search}")
    public ResponseEntity<ResponseDTO<List<LodgingDTO>>> getLodgingsBySearch(@PathVariable String search) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Alojamientos obtenidos satisfactoriamente.", lodgingsUseCases.getLodgingsBySearch(search)));
    }

    @GetMapping("/{lodgingUUID}/metrics")
    public ResponseEntity<ResponseDTO<LodgingMetricsDTO>> getLodgingMetrics(@PathVariable String lodgingUUID) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Métricas obtenidas satisfactoriamente.",
                lodgingsUseCases.getLodgingsMetrics(UUID.fromString(lodgingUUID))));
    }

    @GetMapping("/{hostUUID}")
    public ResponseEntity<ResponseDTO<List<LodgingDTO>>> getLodgingsByHost(@PathVariable String hostUUID) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Alojamientos obtenidos satisfactoriamente.",
                lodgingsUseCases.getLodgingsByHost(UUID.fromString(hostUUID))));
    }

    @PatchMapping("/{lodgingUUID}/comments")
    public ResponseEntity<ResponseDTO<Void>> addCommentLodging(
            @PathVariable String lodgingUUID,
            @Valid @RequestBody CommentDTO comment
    ) {
        return ResponseEntity.status(201).body(new ResponseDTO<>(false,
                "Comentario agregado satisfactoriamente.",
                lodgingsUseCases.addCommentLodging(UUID.fromString(lodgingUUID), comment)));
    }

    @PatchMapping("/{lodgingUUID}/comments/{userUUID}/host_response")
    public ResponseEntity<ResponseDTO<Void>> addCommentResponseLodging(
            @PathVariable String lodgingUUID,
            @PathVariable String userUUID,
            @Valid @RequestBody String response
    ) {
        return ResponseEntity.status(201).body(new ResponseDTO<>(false,
                "Respuesta al comentario agregado satisfactoriamente.",
                lodgingsUseCases.addCommentResponseLodging(UUID.fromString(lodgingUUID), UUID.fromString(userUUID),
                        response)));
    }

    @PutMapping("/{lodgingUUID}")
    public ResponseEntity<ResponseDTO<Void>> updateLodging(
            @PathVariable String lodgingUUID,
            @Valid @RequestBody LodgingDTO dto
    ) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Alojamiento actualizado satisfactoriamente.",
                lodgingsUseCases.updateLodging(UUID.fromString(lodgingUUID), dto)));
    }

    @DeleteMapping("/{lodgingUUID}")
    public ResponseEntity<ResponseDTO<Void>> deleteLodging(@PathVariable String lodgingUUID) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Alojamiento eliminado satisfactoriamente.",
                lodgingsUseCases.deleteLodging(UUID.fromString(lodgingUUID))));
    }
}
