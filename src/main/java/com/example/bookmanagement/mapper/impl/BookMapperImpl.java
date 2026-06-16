package com.example.bookmanagement.mapper.impl;

import com.example.bookmanagement.dto.request.BookCreateRequestDto;
import com.example.bookmanagement.dto.request.BookUpdateRequestDto;
import com.example.bookmanagement.dto.response.BookResponseDto;
import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.mapper.BookMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public Book toEntity(BookCreateRequestDto request) {
        return Book.builder()
                .title(request.title())
                .author(request.author())
                .price(request.price())
                .status(request.status())
                .deleted(false)
                .build();
    }

    @Override
    public void updateEntity(Book book, BookUpdateRequestDto request) {
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setPrice(request.price());
        book.setStatus(request.status());
    }

    @Override
    public BookResponseDto toResponse(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getStatus()
        );
    }
}
