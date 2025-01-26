package com.chatting.auth.member.application;

import com.chatting.auth.member.domain.Member;
import com.chatting.auth.member.domain.MemberRepository;
import com.chatting.auth.member.presentation.dto.MemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void save(MemberRequest.MemberSave dto) {
        String username = dto.username();
        if(memberRepository.isExistUsername(username)) throw new RuntimeException("Username is already in use");
        String nickname = dto.nickname();
        if(memberRepository.isExistNickname(nickname)) throw new RuntimeException("Nickname is already in use");

        String password = passwordEncoder.encode(dto.password());
        Member member = MemberRequest.MemberSave.toEntity(dto, password);
        if(member == null) throw new RuntimeException("Server Error (Password is not encoded)");
        memberRepository.save(member);
    }
}