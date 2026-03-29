package com.library.lms.controller;

import com.library.lms.entity.Book;
import com.library.lms.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Book CRUD operations.
 *
 * Base URL: /api/books
 *
 * GET    /api/books              – Get all books          (ADMIN, USER)
 * GET    /api/books/{id}         – Get book by ID         (ADMIN, USER)
 * GET    /api/books/available    – Get available books    (ADMIN, USER)
 * GET    /api/books/search       – Search by title        (ADMIN, USER)
 * GET    /api/books/genre        – Filter by genre        (ADMIN, USER)
 * GET    /api/books/author/{id}  – Books by author        (ADMIN, USER)
 * POST   /api/books              – Create book            (ADMIN only)
 * PUT    /api/books/{id}         – Update book            (ADMIN only)
 * PUT    /api/books/{id}/author/{authorId} – Assign author (ADMIN only)
 * DELETE /api/books/{id}         – Delete book            (ADMIN only)
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchByTitle(title));
    }

    @GetMapping("/genre")
    public ResponseEntity<List<Book>> getByGenre(@RequestParam String genre) {
        return ResponseEntity.ok(bookService.getBooksByGenre(genre));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book created = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id,
                                           @Valid @RequestBody Book bookDetails) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDetails));
    }

    @PutMapping("/{id}/author/{authorId}")
    public ResponseEntity<Book> assignAuthor(@PathVariable Long id,
                                             @PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.assignAuthor(id, authorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully.");
    }
}
