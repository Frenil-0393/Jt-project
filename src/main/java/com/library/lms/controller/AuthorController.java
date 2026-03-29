package com.library.lms.controller;

import com.library.lms.entity.Author;
import com.library.lms.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Author CRUD operations.
 *
 * Base URL: /api/authors
 *
 * GET    /api/authors          – Get all authors       (ADMIN, USER)
 * GET    /api/authors/{id}     – Get author by ID      (ADMIN, USER)
 * GET    /api/authors/search   – Search by name        (ADMIN, USER)
 * POST   /api/authors          – Create author         (ADMIN only)
 * PUT    /api/authors/{id}     – Update author         (ADMIN only)
 * DELETE /api/authors/{id}     – Delete author         (ADMIN only)
 */
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Author>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(authorService.searchByName(name));
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) {
        Author created = authorService.createAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id,
                                               @Valid @RequestBody Author authorDetails) {
        return ResponseEntity.ok(authorService.updateAuthor(id, authorDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok("Author deleted successfully.");
    }
}
