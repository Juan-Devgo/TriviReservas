package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.domain.ReservationState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ReservationFilters(
        String state,
        String checkIn,
        String checkOut
) {
    public boolean validCheckInOut() {
        boolean validCheckInOut = true;

        if(checkIn != null && checkOut != null) {
            LocalDateTime dateTimeCheckIn = LocalDateTime.parse(checkIn, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime dateTimeCheckOut = LocalDateTime.parse(checkOut, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            validCheckInOut = dateTimeCheckIn.isBefore(dateTimeCheckOut);
        }

        return validCheckInOut;
    }

    public boolean validState() {
        boolean validState = true;

        try {
            ReservationState.valueOf(state);
        } catch (IllegalArgumentException e) {
            validState = false;
        }

        return validState;
    }
}
