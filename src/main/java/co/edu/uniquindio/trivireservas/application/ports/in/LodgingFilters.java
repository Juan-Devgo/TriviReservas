package co.edu.uniquindio.trivireservas.application.ports.in;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record LodgingFilters(
        String city,
        Integer minPrice,
        Integer maxPrice,
        String checkIn,
        String checkOut
) {
    public boolean validPriceRange() {
        boolean validPrice = true;

        if(minPrice != null && maxPrice != null) {
            validPrice = minPrice <= maxPrice;
        }

        return validPrice;
    }

    public boolean validCheckInOut() {
        boolean validCheckInOut = true;

        if(checkIn != null && checkOut != null) {
            LocalDateTime dateTimeNow = LocalDateTime.now();
            LocalDateTime dateTimeCheckIn = LocalDateTime.parse(checkIn, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime dateTimeCheckOut = LocalDateTime.parse(checkOut, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            validCheckInOut = dateTimeCheckIn.isBefore(dateTimeCheckOut)
                    && dateTimeNow.isBefore(dateTimeCheckIn)
                    && dateTimeNow.plusDays(1).isBefore(dateTimeCheckOut);
        }

        if(checkIn != null && checkOut == null) {
            LocalDateTime dateTimeCheckIn = LocalDateTime.parse(checkIn, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            validCheckInOut = LocalDateTime.now().isBefore(dateTimeCheckIn);
        }

        if(checkIn == null && checkOut != null) {
            LocalDateTime dateTimeCheckOut = LocalDateTime.parse(checkOut, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            validCheckInOut = LocalDateTime.now().plusDays(1).isBefore(dateTimeCheckOut);
        }

        return validCheckInOut;
    }
}
