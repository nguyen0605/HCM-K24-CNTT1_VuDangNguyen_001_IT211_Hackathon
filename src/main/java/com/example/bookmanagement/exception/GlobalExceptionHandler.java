package com.example.bookmanagement.exception;

import com.example.bookmanagement.dto.response.ApiErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDto> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(
                new ApiErrorResponseDto(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Dữ liệu không hợp lệ",
                        errors
                )
        );
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiErrorResponseDto> handleBadRequest(Exception exception) {
        return ResponseEntity.badRequest().body(
                new ApiErrorResponseDto(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Tham số yêu cầu không hợp lệ",
                        Map.of("request", exception.getMessage())
                )
        );
    }

    @ExceptionHandler(InvalidBookPatchException.class)
    public ResponseEntity<ApiErrorResponseDto> handleInvalidPatch(InvalidBookPatchException exception) {
        return ResponseEntity.badRequest().body(
                new ApiErrorResponseDto(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage(),
                        Map.of()
                )
        );
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDto> handleBookNotFound(BookNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorResponseDto(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        exception.getMessage(),
                        Map.of()
                )
        );
    }
}
