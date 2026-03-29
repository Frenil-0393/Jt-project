package com.library.lms.service;

import com.library.lms.entity.Member;
import com.library.lms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    public Member createMember(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new RuntimeException("Member with email already exists: " + member.getEmail());
        }
        return memberRepository.save(member);
    }

    public Member updateMember(Long id, Member memberDetails) {
        Member member = getMemberById(id);
        // Check email uniqueness if email is being changed
        if (memberDetails.getEmail() != null
                && !memberDetails.getEmail().equals(member.getEmail())) {
            memberRepository.findByEmail(memberDetails.getEmail()).ifPresent(existing -> {
                throw new RuntimeException("Email already in use by another member: " + memberDetails.getEmail());
            });
        }
        member.setName(memberDetails.getName());
        member.setEmail(memberDetails.getEmail());
        member.setPhone(memberDetails.getPhone());
        member.setActive(memberDetails.isActive());
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
    }

    public List<Member> searchByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Member> getActiveMembers() {
        return memberRepository.findByActive(true);
    }
}
