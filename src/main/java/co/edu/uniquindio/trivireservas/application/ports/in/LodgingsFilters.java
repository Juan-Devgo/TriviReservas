package co.edu.uniquindio.trivireservas.application.ports.in;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record  LodgingsFilters(
        String city,
        Double minPrice,
        Double maxPrice,
        LocalDateTime checkIn,
        LocalDateTime checkOut
) {
    public boolean validPriceRange() {
        boolean validPrice = true;

        if (minPrice != null && maxPrice != null) {
            validPrice = minPrice <= maxPrice;
        }

        return validPrice;
    }

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
}
