package com.example.bookmanagement.controller;

import com.example.bookmanagement.dto.request.BookCreateRequestDto;
import com.example.bookmanagement.dto.request.BookPatchRequestDto;
import com.example.bookmanagement.dto.request.BookUpdateRequestDto;
import com.example.bookmanagement.dto.response.BookResponseDto;
import com.example.bookmanagement.dto.response.PageResponseDto;
import com.example.bookmanagement.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDto create(@Valid @RequestBody BookCreateRequestDto request) {
        return bookService.create(request);
    }

    @GetMapping
    public PageResponseDto<BookResponseDto> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Số trang phải lớn hơn hoặc bằng 0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Kích thước trang phải lớn hơn 0") int size
    ) {
        return bookService.getAll(title, author, page, size);
    }

    @GetMapping("/{id}")
    public BookResponseDto getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PutMapping("/{id}")
    public BookResponseDto update(@PathVariable Long id, @Valid @RequestBody BookUpdateRequestDto request) {
        return bookService.update(id, request);
    }

    @PatchMapping("/{id}")
    public BookResponseDto patch(@PathVariable Long id, @Valid @RequestBody BookPatchRequestDto request) {
        return bookService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
