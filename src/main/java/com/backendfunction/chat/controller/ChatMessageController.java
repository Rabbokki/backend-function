package com.backendfunction.chat.controller;

import com.backendfunction.chat.dto.ChatDto;
import com.backendfunction.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void enter(ChatDto.CreateRequest request) {
        log.info("Received chat message: roomId={}, roomName={}, sender={}, message={}, timestamp={}",
                request.getRoomId(), request.getRoomName(), request.getSender(), request.getMessage(), request.getTimestamp());
        try {
            String roomName = chatMessageService.save(request);
            String destination = "/sub/chatroom/" + roomName;
            log.info("Broadcasting to: {}", destination);

            // timestamp 파싱
            LocalDateTime createAt = LocalDateTime.now();
            if (request.getTimestamp() != null && !request.getTimestamp().isEmpty()) {
                try {
                    createAt = LocalDateTime.parse(request.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME);
                } catch (Exception e) {
                    log.warn("Failed to parse timestamp: {}, using current time", request.getTimestamp());
                }
            }

            ChatDto.Response response = ChatDto.Response.builder()
                    .roomId(request.getRoomId())
                    .roomName(roomName)
                    .sender(request.getSender())
                    .message(request.getMessage())
                    .timestamp(request.getTimestamp())
                    .createAt(createAt)
                    .build();

            sendingOperations.convertAndSend(destination, response);
        } catch (Exception e) {
            log.error("Failed to process chat message: roomId={}, error={}", request.getRoomId(), e.getMessage(), e);
            throw new RuntimeException("Failed to process chat message: " + e.getMessage());
        }
    }
}