package com.chatting.auth.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaRefreshTokenInfo {
    Long id;
    String username;
    String nickname;
    String token;
    RefreshTokenState state;
}
