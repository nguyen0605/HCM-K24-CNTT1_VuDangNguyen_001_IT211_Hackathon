package com.example.bookmanagement.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponseDto(
        LocalDateTime timestamp,
        int status,
        String message,
        Map<String, String> errors
) {
}
