package com.example.bookmanagement.controller;

import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.BookStatus;
import com.example.bookmanagement.repository.BookRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void createBookShouldReturnCreated() throws Exception {
        String requestBody = """
                {
                  "title": "Lập trình Java hiện đại",
                  "author": "Nguyễn Minh Tâm",
                  "price": 150.00,
                  "status": "AVAILABLE"
                }
                """;

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Lập trình Java hiện đại"))
                .andExpect(jsonPath("$.author").value("Nguyễn Minh Tâm"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void createBookShouldReturnBadRequestWhenInvalid() throws Exception {
        String requestBody = """
                {
                  "title": "",
                  "author": "",
                  "price": 0,
                  "status": "AVAILABLE"
                }
                """;

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dữ liệu không hợp lệ"))
                .andExpect(jsonPath("$.errors.title").value("Tiêu đề không được để trống"))
                .andExpect(jsonPath("$.errors.author").value("Tác giả không được để trống"))
                .andExpect(jsonPath("$.errors.price").value("Giá sách phải lớn hơn 0"));
    }

    @Test
    void getAllShouldSupportSearchAndPagination() throws Exception {
        bookRepository.save(Book.builder()
                .title("Thực hành Spring Boot")
                .author("Nguyễn Lan")
                .price(new BigDecimal("99.00"))
                .status(BookStatus.AVAILABLE)
                .deleted(false)
                .build());
        bookRepository.save(Book.builder()
                .title("Java nâng cao")
                .author("Trần Minh")
                .price(new BigDecimal("120.00"))
                .status(BookStatus.BORROWED)
                .deleted(false)
                .build());

        mockMvc.perform(get("/api/v1/books")
                        .param("title", "Spring")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Thực hành Spring Boot"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void updateAndPatchShouldWork() throws Exception {
        Book savedBook = bookRepository.save(Book.builder()
                .title("Tên cũ")
                .author("Tác giả cũ")
                .price(new BigDecimal("50.00"))
                .status(BookStatus.AVAILABLE)
                .deleted(false)
                .build());

        String putBody = """
                {
                  "title": "Tên mới",
                  "author": "Tác giả mới",
                  "price": 70.00,
                  "status": "BORROWED"
                }
                """;

        mockMvc.perform(put("/api/v1/books/{id}", savedBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(putBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Tên mới"))
                .andExpect(jsonPath("$.status").value("BORROWED"));

        String patchBody = """
                {
                  "author": "Tác giả đã cập nhật"
                }
                """;

        mockMvc.perform(patch("/api/v1/books/{id}", savedBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Tác giả đã cập nhật"))
                .andExpect(jsonPath("$.title").value("Tên mới"));
    }

    @Test
    void deleteShouldSoftDeleteBook() throws Exception {
        Book savedBook = bookRepository.save(Book.builder()
                .title("Thiết kế phần mềm theo miền nghiệp vụ")
                .author("Hoàng Đức Anh")
                .price(new BigDecimal("200.00"))
                .status(BookStatus.AVAILABLE)
                .deleted(false)
                .build());

        mockMvc.perform(delete("/api/v1/books/{id}", savedBook.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/books/{id}", savedBook.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Không tìm thấy sách với id = " + savedBook.getId()));
    }

    @Test
    void getByIdShouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Không tìm thấy sách với id = 999"));
    }
}
