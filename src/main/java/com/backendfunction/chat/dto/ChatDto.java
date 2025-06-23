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
    public static class CreateRequest {
        @NotNull
        private Long roomId;
        private String roomName;
        @NotBlank
        private String message;
        private String sender;
        private String timestamp;
    }

    @Getter
    public static class CreateResponse {
        private Long id;
        private String roomName;

        @Builder
        CreateResponse(Long id, String name) {
            this.id = id;
            this.roomName = name;
        }
    }

    @Getter
    public static class Response {
        private Long id;
        private Long roomId; // 추가
        private String roomName; // 추가
        private String sender;
        private String message;
        private String timestamp; // 프론트엔드 호환성
        private LocalDateTime createAt;
        private LocalDateTime updatedAt;

        @Builder
        public static Response builder() {
            return new Response();
        }

        public Response id(Long id) {
            this.id = id;
            return this;
        }

        public Response roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public Response roomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Response sender(String sender) {
            this.sender = sender;
            return this;
        }

        public Response message(String message) {
            this.message = message;
            return this;
        }

        public Response timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Response createAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        public Response updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Response build() {
            return this;
        }

        // ChatMessage 기반 생성 메서드
        public static Response fromChatMessage(ChatMessage chatMessage, Long roomId, String roomName, String timestamp) {
            return Response.builder()
                    .id(chatMessage.getId())
                    .roomId(roomId)
                    .roomName(roomName)
                    .sender(chatMessage.getSender())
                    .message(chatMessage.getMessage())
                    .timestamp(timestamp)
                    .createAt(chatMessage.getCreatedAt())
                    .updatedAt(chatMessage.getUpdatedAt())
                    .build();
        }
    }
}
