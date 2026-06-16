package com.example.bookmanagement.dto.request;

import com.example.bookmanagement.entity.BookStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record BookUpdateRequestDto(
        @NotBlank(message = "Tiêu đề không được để trống")
        String title,

        @NotBlank(message = "Tác giả không được để trống")
        String author,

        @NotNull(message = "Giá sách không được để trống")
        @DecimalMin(value = "0.01", message = "Giá sách phải lớn hơn 0")
        BigDecimal price,

        @NotNull(message = "Trạng thái sách không được để trống")
        BookStatus status
) {
}
