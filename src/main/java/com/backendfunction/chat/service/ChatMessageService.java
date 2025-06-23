package com.backendfunction.chat.service;

import com.backendfunction.chat.dto.ChatDto;
import com.backendfunction.chat.dto.ChatSimpleDto;
import com.backendfunction.chat.entity.ChatMessage;
import com.backendfunction.chat.entity.ChatRoom;
import com.backendfunction.chat.repository.ChatMessageRepository;
import com.backendfunction.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public String save(ChatDto.CreateRequest request) {
        log.info("Saving message: roomId={}, roomName={}, sender={}, message={}",
                request.getRoomId(), request.getRoomName(), request.getSender(), request.getMessage());
        try {
            if (request.getRoomId() == null) {
                log.error("Invalid roomId: null");
                throw new IllegalArgumentException("Room ID cannot be null");
            }
            ChatRoom room = getRoom(request.getRoomId());
            ChatMessage message = ChatMessage.createMessage(request, room);
            ChatSimpleDto chatSimpleDto = ChatSimpleDto.builder()
                    .message(message.getMessage())
                    .sender(message.getSender())
                    .message_check_status((byte) 0)
                    .roomId(request.getRoomId())
                    .build();
            chatMessageRepository.save(message);
            log.info("Message saved: roomName={}", room.getRoomName());
            return room.getRoomName();
        } catch (Exception e) {
            log.error("Failed to save message: roomId={}, error={}", request.getRoomId(), e.getMessage(), e);
            throw e;
        }
    }

    private ChatRoom getRoom(Long roomId) {
        log.info("Fetching room: roomId={}", roomId);
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("Chat room not found: roomId={}", roomId);
                    return new RuntimeException("채팅방이 없습니다: roomId=" + roomId);
                });
    }
}
