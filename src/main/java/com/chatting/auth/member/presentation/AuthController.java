package com.chatting.auth.member.presentation;

import com.chatting.auth.member.domain.MemberDetails;
import com.chatting.auth.security.JwtProvider;
import com.chatting.auth.security.refreshToken.KafkaRefreshTokenInfo;
import com.chatting.auth.security.refreshToken.KafkaRefreshTokenProducer;
import com.chatting.auth.security.refreshToken.RefreshTokenState;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final KafkaRefreshTokenProducer kafkaRefreshTokenProducer;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie(JwtProvider.REFRESH_HEADER_STRING, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof MemberDetails memberDetails) {
            KafkaRefreshTokenInfo refreshTokenInfo = KafkaRefreshTokenInfo.builder()
                    .id(memberDetails.getMemberId())
                    .username("")
                    .nickname("")
                    .token("")
                    .state(RefreshTokenState.LOGOUT)
                    .build();
            kafkaRefreshTokenProducer.sendRefreshTokenInfo(refreshTokenInfo);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
