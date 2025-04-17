package com.backendfunction.chat.entity;

import com.backendfunction.account.entity.BaseEntity;
import com.backendfunction.chat.dto.ChatDto;
import com.backendfunction.chat.dto.ChatDto.CreateRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@CrossOrigin("*")
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    private String sender;
    private String message;
    private boolean messageCheckStatus;

    public static ChatMessage createMessage(CreateRequest request, ChatRoom room){
        return ChatMessage.builder()
                .sender(request.getSender())
                .message(request.getMessage())
                .room(room)
                .build();
    }
}
