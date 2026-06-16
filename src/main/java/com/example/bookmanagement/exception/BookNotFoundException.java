package com.example.bookmanagement.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Không tìm thấy sách với id = " + id);
    }
}
