package co.edu.uniquindio.trivireservas.application.dto;

import java.util.List;

public record PageResponse<T>( // TODO poner en open API
        List<T> content,
        int page,
        int pageSize,
        int totalPages,
        boolean last
) {}
