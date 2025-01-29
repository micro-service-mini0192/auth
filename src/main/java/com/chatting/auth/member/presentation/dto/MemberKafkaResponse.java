package com.chatting.auth.member.presentation.dto;

import com.chatting.auth.member.domain.Member;
import lombok.Builder;

public class MemberKafkaResponse {
    @Builder
    public record MemberInfo(
            Long id,
            String username,
            String nickname
    ) {
        public static MemberResponse.MemberInfo toDto(Member member) {
            return MemberResponse.MemberInfo.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .build();
        }
    }
}
