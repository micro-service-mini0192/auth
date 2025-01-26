package com.chatting.auth.member.presentation;

import com.chatting.auth.member.application.MemberService;
import com.chatting.auth.member.domain.Member;
import com.chatting.auth.member.domain.MemberDetails;
import com.chatting.auth.member.presentation.dto.MemberRequest;
import com.chatting.auth.member.presentation.dto.MemberResponse;
import com.chatting.auth.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody MemberRequest.MemberSave dto) {
        memberService.save(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<MemberResponse.MemberInfo> auth(@RequestBody MemberRequest.Authed dto) {
        MemberDetails memberDetails = jwtProvider.getMemberDetails(dto.token());
        MemberResponse.MemberInfo memberInfo = MemberResponse.MemberInfo.builder()
                .id(memberDetails.getMemberId())
                .username(memberDetails.getUsername())
                .nickname(memberDetails.getNickname())
                .build();
        return ResponseEntity.ok().body(memberInfo);
    }
}