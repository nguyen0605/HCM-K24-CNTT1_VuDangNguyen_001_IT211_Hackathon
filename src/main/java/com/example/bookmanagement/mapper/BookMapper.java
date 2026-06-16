package com.example.bookmanagement.mapper;

import com.example.bookmanagement.dto.request.BookCreateRequestDto;
import com.example.bookmanagement.dto.request.BookUpdateRequestDto;
import com.example.bookmanagement.dto.response.BookResponseDto;
import com.example.bookmanagement.entity.Book;

public interface BookMapper {

    Book toEntity(BookCreateRequestDto request);

    void updateEntity(Book book, BookUpdateRequestDto request);

    BookResponseDto toResponse(Book book);
}
