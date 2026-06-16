package com.example.bookmanagement.service.impl;

import com.example.bookmanagement.dto.request.BookCreateRequestDto;
import com.example.bookmanagement.dto.request.BookPatchRequestDto;
import com.example.bookmanagement.dto.request.BookUpdateRequestDto;
import com.example.bookmanagement.dto.response.BookResponseDto;
import com.example.bookmanagement.dto.response.PageResponseDto;
import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.exception.BookNotFoundException;
import com.example.bookmanagement.exception.InvalidBookPatchException;
import com.example.bookmanagement.mapper.BookMapper;
import com.example.bookmanagement.repository.BookRepository;
import com.example.bookmanagement.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookResponseDto create(BookCreateRequestDto request) {
        Book book = bookMapper.toEntity(request);
        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<BookResponseDto> getAll(String title, String author, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> result = bookRepository.searchActiveBooks(normalize(title), normalize(author), pageable);

        return new PageResponseDto<>(
                result.map(bookMapper::toResponse).getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDto getById(Long id) {
        return bookMapper.toResponse(getActiveBook(id));
    }

    @Override
    public BookResponseDto update(Long id, BookUpdateRequestDto request) {
        Book book = getActiveBook(id);
        bookMapper.updateEntity(book, request);
        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public BookResponseDto patch(Long id, BookPatchRequestDto request) {
        Book book = getActiveBook(id);

        if (request.title() != null) {
            if (request.title().isBlank()) {
                throw new InvalidBookPatchException("Tiêu đề không được để trống");
            }
            book.setTitle(request.title());
        }

        if (request.author() != null) {
            if (request.author().isBlank()) {
                throw new InvalidBookPatchException("Tác giả không được để trống");
            }
            book.setAuthor(request.author());
        }

        if (request.price() != null) {
            book.setPrice(request.price());
        }

        if (request.status() != null) {
            book.setStatus(request.status());
        }

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        Book book = getActiveBook(id);
        book.setDeleted(true);
        bookRepository.save(book);
    }

    private Book getActiveBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (book.isDeleted()) {
            throw new BookNotFoundException(id);
        }

        return book;
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
