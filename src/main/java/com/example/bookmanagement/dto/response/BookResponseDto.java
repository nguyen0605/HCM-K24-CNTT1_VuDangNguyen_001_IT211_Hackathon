package com.example.bookmanagement.dto.response;

import com.example.bookmanagement.entity.BookStatus;
import java.math.BigDecimal;

public record BookResponseDto(
        Long id,
        String title,
        String author,
        BigDecimal price,
        BookStatus status
) {
}
