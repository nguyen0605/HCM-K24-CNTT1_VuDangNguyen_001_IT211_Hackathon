package com.example.bookmanagement.dto.request;

import com.example.bookmanagement.entity.BookStatus;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public record BookPatchRequestDto(
        String title,
        String author,

        @DecimalMin(value = "0.01", message = "Giá sách phải lớn hơn 0")
        BigDecimal price,

        BookStatus status
) {
}
