package com.chatting.auth.member.presentation.dto;

import com.chatting.auth.member.domain.Member;
import com.chatting.auth.member.domain.MemberRole;
import lombok.Builder;

public class MemberResponse {
    @Builder
    public record MemberInfo(
            Long id,
            String username,
            String nickname
    ) {
        public static MemberInfo toDto(Member member) {
            return MemberInfo.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .build();
        }
    }
}