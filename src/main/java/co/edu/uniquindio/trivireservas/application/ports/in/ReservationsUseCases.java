package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.ReservationDTO;

import java.util.List;
import java.util.UUID;

public interface ReservationsUseCases {

    List<ReservationDTO> getReservationsByLodging(UUID lodgingUUID);

    List<ReservationDTO> getReservationsByUser(UUID userUUID);

    void createReservation(UUID userUUID, UUID LodgingUUID, ReservationDTO dto);


}
