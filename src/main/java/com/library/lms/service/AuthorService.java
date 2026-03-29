package com.library.lms.service;

import com.library.lms.entity.Author;
import com.library.lms.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
    }

    public Author createAuthor(Author author) {
        if (author.getEmail() != null && authorRepository.findByEmail(author.getEmail()).isPresent()) {
            throw new RuntimeException("Author with email already exists: " + author.getEmail());
        }
        return authorRepository.save(author);
    }

    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = getAuthorById(id);
        // Check email uniqueness if email is being changed
        if (authorDetails.getEmail() != null
                && !authorDetails.getEmail().equals(author.getEmail())) {
            authorRepository.findByEmail(authorDetails.getEmail()).ifPresent(existing -> {
                throw new RuntimeException("Email already in use by another author: " + authorDetails.getEmail());
            });
        }
        author.setName(authorDetails.getName());
        author.setEmail(authorDetails.getEmail());
        author.setBio(authorDetails.getBio());
        return authorRepository.save(author);
    }

    public void deleteAuthor(Long id) {
        Author author = getAuthorById(id);
        authorRepository.delete(author);
    }

    public List<Author> searchByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }
}
