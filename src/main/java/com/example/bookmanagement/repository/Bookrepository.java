package com.example.bookmanagement.repository;

import com.example.bookmanagement.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
            select b
            from Book b
            where b.deleted = false
              and (:title is null or lower(b.title) like lower(concat('%', :title, '%')))
              and (:author is null or lower(b.author) like lower(concat('%', :author, '%')))
            """)
    Page<Book> searchActiveBooks(@Param("title") String title, @Param("author") String author, Pageable pageable);
}
