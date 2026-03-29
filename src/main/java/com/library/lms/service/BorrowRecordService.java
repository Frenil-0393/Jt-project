package com.library.lms.service;

import com.library.lms.entity.Book;
import com.library.lms.entity.BorrowRecord;
import com.library.lms.entity.BorrowRecord.BorrowStatus;
import com.library.lms.entity.Member;
import com.library.lms.repository.BookRepository;
import com.library.lms.repository.BorrowRecordRepository;
import com.library.lms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowRecordService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }

    public BorrowRecord getBorrowRecordById(Long id) {
        return borrowRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + id));
    }

    /**
     * Borrow a book: decrements availableCopies and creates a BorrowRecord.
     */
    public BorrowRecord borrowBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));

        if (!member.isActive()) {
            throw new RuntimeException("Member is not active and cannot borrow books.");
        }
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No available copies of: " + book.getTitle());
        }

        // Reduce available count
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setMember(member);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        record.setStatus(BorrowStatus.BORROWED);

        return borrowRecordRepository.save(record);
    }

    /**
     * Return a book: increments availableCopies and updates BorrowRecord status.
     */
    public BorrowRecord returnBook(Long borrowRecordId) {
        BorrowRecord record = getBorrowRecordById(borrowRecordId);

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("Book already returned for record id: " + borrowRecordId);
        }

        record.setReturnDate(LocalDate.now());
        record.setStatus(BorrowStatus.RETURNED);

        // Increment available copies
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowRecordRepository.save(record);
    }

    public List<BorrowRecord> getBorrowsByMember(Long memberId) {
        return borrowRecordRepository.findByMemberId(memberId);
    }

    public List<BorrowRecord> getBorrowsByBook(Long bookId) {
        return borrowRecordRepository.findByBookId(bookId);
    }

    public List<BorrowRecord> getBorrowsByStatus(BorrowStatus status) {
        return borrowRecordRepository.findByStatus(status);
    }

    public void deleteBorrowRecord(Long id) {
        BorrowRecord record = getBorrowRecordById(id);
        borrowRecordRepository.delete(record);
    }
}
