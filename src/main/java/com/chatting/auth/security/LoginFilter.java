package com.chatting.auth.security;

import com.chatting.auth.exception.ExceptionResponseSender;
import com.chatting.auth.member.domain.MemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.UUID;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final KafkaRefreshTokenProducer kafkaRefreshTokenProducer;

    public LoginFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, KafkaRefreshTokenProducer kafkaRefreshTokenProducer) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.kafkaRefreshTokenProducer = kafkaRefreshTokenProducer;

        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/v1/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        Authentication token = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(token);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        MemberDetails member = (MemberDetails) authResult.getPrincipal();

        String jwtToken = jwtProvider.createJwtToken(member);
        response.addHeader(jwtProvider.JWT_HEADER_STRING, jwtProvider.TOKEN_PREFIX_JWT + jwtToken);

        String refreshToken = UUID.randomUUID().toString();

        KafkaRefreshTokenInfo refreshTokenInfo = KafkaRefreshTokenInfo.builder()
                        .id(member.getMemberId())
                        .username(member.getUsername())
                        .nickname(member.getNickname())
                        .token(refreshToken)
                        .state(RefreshTokenState.LOGIN)
                        .build();

        kafkaRefreshTokenProducer.sendRefreshTokenInfo(refreshTokenInfo);
        response.setStatus(HttpStatus.CREATED.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        ExceptionResponseSender.createExceptionResponse(HttpStatus.UNAUTHORIZED.value(), request, response, "로그인 실패");
    }
}
