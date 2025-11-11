package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.lodging.*;
import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import co.edu.uniquindio.trivireservas.application.ports.in.LodgingsFilters;
import co.edu.uniquindio.trivireservas.application.ports.in.LodgingsUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lodgings")
public class LodgingController {

    private final LodgingsUseCases lodgingsUseCases;

    @GetMapping
    public ResponseEntity<ResponseDTO<PageResponse<LodgingDTO>>> getLodgings(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime checkIn,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime checkOut,
            @RequestParam(defaultValue = "0") int page
    ) {
        log.info("Request received to get all the lodgings. (GET /api/lodgings)");
        log.info("Filters - city: {}, minPrice: {}, maxPrice: {}, checkIn: {}, checkOut: {}, page: {}",
                city, minPrice, maxPrice, checkIn, checkOut, page);
        LodgingsFilters filters = new LodgingsFilters(city, minPrice, maxPrice, checkIn, checkOut);

        if(!filters.validCheckInOut() || !filters.validPriceRange()) {
            throw new IllegalArgumentException("Rango de fechas o precios inválido tratando de obtener los alojamientos.");
        }

        PageResponse<LodgingDTO> result = lodgingsUseCases.getLodgings(filters, page);
        return ResponseEntity.ok(new ResponseDTO<>(false, "Alojamientos obtenidos satisfactoriamente.", result));
    }

    @GetMapping("/{lodgingUUID}")
    public ResponseEntity<ResponseDTO<LodgingDTO>> getLodging(@PathVariable String lodgingUUID) {
        log.info("Request received to get a lodging identified by its UUID. (GET /api/lodgings/{lodgingUUID})");
        LodgingDTO result = lodgingsUseCases.getLodging(UUID.fromString(lodgingUUID));
        return ResponseEntity.ok(new ResponseDTO<>(false, "Alojamiento obtenido satisfactoriamente.", result));
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<ResponseDTO<PageResponse<LodgingDTO>>> getLodgingsBySearch(
            @PathVariable String search,
            @RequestParam int page
    ) {
        log.info("Request received to get all the lodgings that matches with a search. (GET /api/lodgings/search/{search})");
        PageResponse<LodgingDTO> result = lodgingsUseCases.getLodgingsBySearch(search, page);
        return ResponseEntity.ok(new ResponseDTO<>(false, "Alojamientos obtenidos satisfactoriamente.", result));
    }

    @GetMapping("/metrics/{lodgingUUID}")
    public ResponseEntity<ResponseDTO<LodgingMetricsDTO>> getLodgingMetrics(@PathVariable String lodgingUUID) {
        log.info("Request received to get all the metrics of an specific lodging. (GET /api/lodgings/metrics/{lodgingUUID})");
        LodgingMetricsDTO result = lodgingsUseCases.getLodgingsMetrics(UUID.fromString(lodgingUUID));
        return ResponseEntity.ok(new ResponseDTO<>(false, "Métricas obtenidas satisfactoriamente.", result));
    }

    @GetMapping("/host/{hostUUID}")
    public ResponseEntity<ResponseDTO<PageResponse<LodgingDTO>>> getLodgingsByHost(
            @PathVariable String hostUUID,
            @RequestParam int page
    ) {
        log.info("Request received to get all the lodgings owned by a specific host. (GET /api/lodgings/host/{hostUUID})");
        PageResponse<LodgingDTO> result = lodgingsUseCases.getLodgingsByHostUUID(UUID.fromString(hostUUID), page);
        return ResponseEntity.ok(new ResponseDTO<>(false, "Alojamientos obtenidos satisfactoriamente.", result));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> createLodging(@Valid @RequestBody CreateLodgingDTO dto) {
        log.info("Request received to create a new lodging. (POST /api/lodgings)");
        return ResponseEntity.status(201).body(new ResponseDTO<>(false,
                "Alojamiento creado satisfactoriamente.",
                lodgingsUseCases.createLodging(dto)));
    }

    @PatchMapping("/{lodgingUUID}/comments")
    public ResponseEntity<ResponseDTO<Void>> addCommentLodging(
            @Valid @RequestBody CreateCommentDTO comment
    ) {
        log.info("Request received to add a new comment to an specific lodging. (PATCH /api/lodgings/{lodgingUUID}/comments)");
        return ResponseEntity.status(201)
                .body(new ResponseDTO<>(false, "Comentario agregado satisfactoriamente.",
                        lodgingsUseCases.addCommentLodging(comment)));
    }

    @PatchMapping("/{lodgingUUID}/comments/{userUUID}/host_response")
    public ResponseEntity<ResponseDTO<Void>> addCommentResponseLodging(
            @PathVariable String lodgingUUID,
            @PathVariable String userUUID,
            @Valid @RequestBody String response
    ) {
        log.info("Request received to add a response for a comment on a specific lodging. (PATCH /api/lodgings/{lodgingUUID}/comments/{userUUID}/host_response)");
        return ResponseEntity.status(201)
                .body(new ResponseDTO<>(false, "Respuesta al comentario agregada satisfactoriamente.",
                        lodgingsUseCases.addCommentResponseLodging(
                        UUID.fromString(lodgingUUID),
                        UUID.fromString(userUUID),
                        response
                )));
    }

    @PutMapping("/{lodgingUUID}")
    public ResponseEntity<ResponseDTO<Void>> updateLodging(
            @PathVariable String lodgingUUID,
            @Valid @RequestBody LodgingDTO dto
    ) {
        log.info("Request received to edit a specific lodging. (PUT /api/lodgings/{lodgingUUID})");
        return ResponseEntity.ok(new ResponseDTO<>(false, "Alojamiento actualizado satisfactoriamente.",
                lodgingsUseCases.updateLodging(UUID.fromString(lodgingUUID), dto)));
    }

    @DeleteMapping("/{lodgingUUID}")
    public ResponseEntity<ResponseDTO<Void>> deleteLodging(@PathVariable String lodgingUUID) {
        log.info("Request received to soft delete a specific lodging. (DELETE /api/lodgings/{lodgingUUID})");
        return ResponseEntity.ok(new ResponseDTO<>(false, "Alojamiento eliminado satisfactoriamente.",
                lodgingsUseCases.deleteLodging(UUID.fromString(lodgingUUID))));
    }
}
