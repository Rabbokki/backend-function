package com.backendfunction.chat.service;

import com.backendfunction.chat.dto.ChatDto;
import com.backendfunction.chat.dto.ChatSimpleDto;
import com.backendfunction.chat.entity.ChatMessage;
import com.backendfunction.chat.entity.ChatRoom;
import com.backendfunction.chat.repository.ChatMessageRepository;
import com.backendfunction.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public String save(ChatDto.CreateRequest request){
        ChatRoom room = getRoom(request.getRoomId());
        ChatMessage message = ChatMessage.createMessage(request,room);
        ChatSimpleDto chatSimpleDto = ChatSimpleDto.builder()
                .message(message.getMessage())
                .sender(message.getSender())
                .message_check_status((byte) 0)
                .roomId(request.getRoomId())
                .build();
        chatMessageRepository.save(message);
        return room.getRoomName();
    }
    private ChatRoom getRoom(Long roomId){
        return chatRoomRepository.findById(roomId)
                .orElseThrow(()->new RuntimeException("채팅방이 없습니다."));
    }
}
