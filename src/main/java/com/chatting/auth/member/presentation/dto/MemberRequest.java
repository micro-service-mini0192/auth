package com.chatting.auth.member.presentation.dto;

import com.chatting.auth.member.domain.Member;
import lombok.Builder;

public class MemberRequest {
    @Builder
    public record MemberSave(
            String username,
            String password,
            String nickname
    ) {
        public static Member toEntity(MemberSave dto, String password) {
            if(password.equals(dto.password)) return null;
            return Member.builder()
                    .username(dto.username)
                    .nickname(dto.nickname)
                    .password(password)
                    .build();
        }
    }

    @Builder
    public record Authed(
            String token
    ) {}
}