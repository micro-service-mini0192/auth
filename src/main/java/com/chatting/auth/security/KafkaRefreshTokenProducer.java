package com.chatting.auth.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaRefreshTokenProducer {

    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendRefreshTokenInfo(KafkaRefreshTokenInfo refreshTokenInfo) {
        try {
            String message = mapper.writeValueAsString(refreshTokenInfo);
            CompletableFuture<SendResult<String, String>> feature = kafkaTemplate.send("refreshToken", message);

            feature.thenAccept(result -> {
                log.info("Send refresh token result, Topic: {}, Offset: {}, Partition: {}",
                        result.getProducerRecord().topic(),
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().partition());
            }).exceptionally(ex -> {
                log.error("Send refresh token fail", ex);
                return null;
            });
        } catch (JsonProcessingException e) {
            log.error("Couldn't encode refresh token info", e);
        }
    }
}
