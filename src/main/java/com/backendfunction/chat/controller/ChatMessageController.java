package com.backendfunction.chat.controller;

import com.backendfunction.chat.dto.ChatDto;
import com.backendfunction.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void enter(ChatDto.CreateRequest request) {
        log.info("WebSocket message received: roomId={}, sender={}, message={}",
                request.getRoomId(), request.getSender(), request.getMessage());
        String roomName = chatMessageService.save(request);
        log.info("Broadcasting to: /sub/chatroom{}", roomName);
        sendingOperations.convertAndSend("/sub/chatroom" + roomName, request);
    }
}
