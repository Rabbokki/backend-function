package com.backendfunction.chat.dto;

import com.backendfunction.chat.entity.ChatRoom;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
public class RoomDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateRequest{
        @NotBlank(message = "메시지 전달할 상대방을 입력해주세요")
        private String targetEmail;

        public ChatRoom toEntity(String account, String targetEmail){
            return ChatRoom.builder().
                    roomName(UUID.randomUUID().toString())
                    .sender(account)
                    .receiver(targetEmail)
                    .build();
        }
    }

    @Getter
    public static class Response{
        private Long roomId;
        private String roomName;
        private List<ChatDto.Response> chatList;
        private String email;
        private Long unreadMessageCount;
        private String latestChatMessage;
        private UserResponseDto.UserData userData;

        @Builder
        Response(ChatRoom room, List<ChatDto.Response> chats,
                 Long unreadMessageCount, String email,
                 String latestChatMessage, UserResponseDto userResponseDto){
            this.roomId = room.getId();
            this.roomName = room.getRoomName();
            this.unreadMessageCount = unreadMessageCount;
            this.latestChatMessage = latestChatMessage;
            this.email = email;
            this.chatList = chats;
            if(userResponseDto != null){
                this.userData = userResponseDto.getData();
            }
        }
    }
}
