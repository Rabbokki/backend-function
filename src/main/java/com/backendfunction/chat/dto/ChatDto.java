package com.backendfunction.chat.dto;

import com.backendfunction.chat.entity.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;

@CrossOrigin("*")
public class ChatDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateRequest{
        @NotNull
        private Long roomId;
        private String roomName;
        @NotBlank
        private String message;
        private String sender;
        private String timestamp;
    }

    @Getter
    public static class CreateResponse{
        private Long id;
        private String  roomName;
        @Builder
        CreateResponse(Long id,String name){
            this.id = id;
            this.roomName = name;
        }
    }

    @Getter
    public static class Response{
        private Long id;
        private String sender;
        private String message;
        private LocalDateTime createAt;
        private LocalDateTime updatedAt;

        @Builder Response(ChatMessage chatMessage){
            this.id = chatMessage.getId();
            this.sender = chatMessage.getSender();
            this.message = chatMessage.getMessage();
            this.createAt = chatMessage.getCreatedAt();
            this.updatedAt = chatMessage.getUpdatedAt();
        }
    }
}
