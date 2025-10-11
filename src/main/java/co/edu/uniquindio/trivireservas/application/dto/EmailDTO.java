package co.edu.uniquindio.trivireservas.application.dto;

public record EmailDTO(
        String subject,
        String body,
        String recipient
) {}
