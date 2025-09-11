package co.edu.uniquindio.trivireservas.application.dto;

public record ResponseDTO<T>(
    boolean error,
    T content
) {}
