package com.library.lms.controller;

import com.library.lms.entity.BorrowRecord;
import com.library.lms.entity.BorrowRecord.BorrowStatus;
import com.library.lms.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Borrow/Return operations.
 *
 * Base URL: /api/borrows
 *
 * GET  /api/borrows                        – Get all borrow records   (ADMIN, USER)
 * GET  /api/borrows/{id}                   – Get record by ID         (ADMIN, USER)
 * GET  /api/borrows/member/{memberId}      – Records by member        (ADMIN, USER)
 * GET  /api/borrows/book/{bookId}          – Records by book          (ADMIN, USER)
 * GET  /api/borrows/status/{status}        – Filter by status         (ADMIN, USER)
 * POST /api/borrows/borrow                 – Borrow a book            (ADMIN, USER)
 * PUT  /api/borrows/return/{id}            – Return a book            (ADMIN, USER)
 * DELETE /api/borrows/{id}                 – Delete a record          (ADMIN only)
 */
@RestController
@RequestMapping("/api/borrows")
public class BorrowRecordController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    @GetMapping
    public ResponseEntity<List<BorrowRecord>> getAllBorrowRecords() {
        return ResponseEntity.ok(borrowRecordService.getAllBorrowRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRecord> getBorrowRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowRecordService.getBorrowRecordById(id));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BorrowRecord>> getByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(borrowRecordService.getBorrowsByMember(memberId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowRecord>> getByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(borrowRecordService.getBorrowsByBook(bookId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BorrowRecord>> getByStatus(@PathVariable BorrowStatus status) {
        return ResponseEntity.ok(borrowRecordService.getBorrowsByStatus(status));
    }

    /**
     * Borrow a book.
     * Query params: bookId, memberId
     * Example: POST /api/borrows/borrow?bookId=1&memberId=2
     */
    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecord> borrowBook(@RequestParam Long bookId,
                                                   @RequestParam Long memberId) {
        return ResponseEntity.ok(borrowRecordService.borrowBook(bookId, memberId));
    }

    /**
     * Return a borrowed book.
     * Example: PUT /api/borrows/return/3
     */
    @PutMapping("/return/{id}")
    public ResponseEntity<BorrowRecord> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(borrowRecordService.returnBook(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBorrowRecord(@PathVariable Long id) {
        borrowRecordService.deleteBorrowRecord(id);
        return ResponseEntity.ok("Borrow record deleted successfully.");
    }
}
