package com.chatting.auth.member.presentation;

import com.chatting.auth.member.presentation.dto.MemberKafkaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class MemberKafkaListener {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @GetMapping
    public void create() {
        MemberKafkaResponse.MemberInfo memberInfo = MemberKafkaResponse.MemberInfo.builder()
                .id(1L)
                .username("TEST")
                .nickname("TEST123")
                .build();

        try {
            String data = objectMapper.writeValueAsString(memberInfo);
            kafkaTemplate.send("auth", data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
