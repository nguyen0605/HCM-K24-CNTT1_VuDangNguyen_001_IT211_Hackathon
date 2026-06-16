package com.example.bookmanagement.service;

import com.example.bookmanagement.dto.request.BookCreateRequestDto;
import com.example.bookmanagement.dto.request.BookPatchRequestDto;
import com.example.bookmanagement.dto.request.BookUpdateRequestDto;
import com.example.bookmanagement.dto.response.BookResponseDto;
import com.example.bookmanagement.dto.response.PageResponseDto;

public interface BookService {

    BookResponseDto create(BookCreateRequestDto request);

    PageResponseDto<BookResponseDto> getAll(String title, String author, int page, int size);

    BookResponseDto getById(Long id);

    BookResponseDto update(Long id, BookUpdateRequestDto request);

    BookResponseDto patch(Long id, BookPatchRequestDto request);

    void delete(Long id);
}
