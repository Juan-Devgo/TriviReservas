package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.domain.ReservationState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ReservationsFilters(
        String state,
        LocalDateTime checkIn,
        LocalDateTime checkOut
) {
    public boolean validCheckInOut() {
        boolean validCheckInOut = true;

        if(checkIn != null && checkOut != null) {
            LocalDateTime dateTimeNow = LocalDateTime.now();

            validCheckInOut = checkIn.isBefore(checkOut)
                    && dateTimeNow.isBefore(checkIn)
                    && dateTimeNow.plusDays(1).isBefore(checkOut);
        }

        if(checkIn != null && checkOut == null) {

            validCheckInOut = LocalDateTime.now().isBefore(checkIn);
        }

        if(checkIn == null && checkOut != null) {

            validCheckInOut = LocalDateTime.now().plusDays(1).isBefore(checkOut);
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
