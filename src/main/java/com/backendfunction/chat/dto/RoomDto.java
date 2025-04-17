package com.backendfunction.chat.dto;

import com.backendfunction.chat.entity.ChatRoom;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
public class RoomDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class CreateRequest{
        private Long postId;
        @NotBlank(message = "메시지 전달할 상대방을 입력해주세요")
        @Email(message = "유효한 이메일 형식이어야 합니다")
        private String targetEmail;

        public ChatRoom toEntity(String sender, String targetEmail) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setSender(sender);
            chatRoom.setReceiver(targetEmail);
            chatRoom.setRoomName(sender + "_" + targetEmail + "_" + System.currentTimeMillis()); // 예시
            return chatRoom;
        }
    }

    @Getter
    public static class Response{
        private Long roomId;
        private String roomName;
        private Long postId;
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
