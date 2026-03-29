package com.library.lms.controller;

import com.library.lms.entity.Member;
import com.library.lms.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Member CRUD operations.
 *
 * Base URL: /api/members
 *
 * GET    /api/members           – Get all members      (ADMIN only)
 * GET    /api/members/{id}      – Get member by ID     (ADMIN only)
 * GET    /api/members/search    – Search by name       (ADMIN only)
 * GET    /api/members/active    – Get active members   (ADMIN only)
 * POST   /api/members           – Create member        (ADMIN only)
 * PUT    /api/members/{id}      – Update member        (ADMIN only)
 * DELETE /api/members/{id}      – Delete member        (ADMIN only)
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Member>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(memberService.searchByName(name));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Member>> getActiveMembers() {
        return ResponseEntity.ok(memberService.getActiveMembers());
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) {
        Member created = memberService.createMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id,
                                               @Valid @RequestBody Member memberDetails) {
        return ResponseEntity.ok(memberService.updateMember(id, memberDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("Member deleted successfully.");
    }
}
