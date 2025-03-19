package com.backendfunction.chat.service;

import com.backendfunction.account.entity.Account;
import com.backendfunction.account.repository.AccountRepository;
import com.backendfunction.chat.dto.ChatDto;
import com.backendfunction.chat.dto.RoomDto;
import com.backendfunction.chat.entity.ChatRoom;
import com.backendfunction.chat.repository.ChatRoomRepository;
import com.backendfunction.global.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public ChatDto.CreateResponse createRoom(RoomDto.CreateRequest request, UserDetailsImpl userDetails) {
        Account account = accountRepository.findAccountWithCartsByEmail(userDetails.getAccount().getEmail())
                .orElseThrow(() -> new RuntimeException("Account를 찾을 수 없습니다."));
        if (checkDuplicatedForCreate(request, account.getEmail())) {
            ChatRoom room = chatRoomRepository.findByReceiverAndSender(request.getTargetEmail(), account.getEmail());
            return ChatDto.CreateResponse.builder().id(room.getId()).name(room.getRoomName()).build();
        } else {
            ChatRoom chatRoom = request.toEntity(account.getEmail(), request.getTargetEmail());
            chatRoom = chatRoomRepository.save(chatRoom);
            return ChatDto.CreateResponse.builder().id(chatRoom.getId()).name(chatRoom.getRoomName()).build();
        }
    }

    private boolean checkDuplicatedForCreate(RoomDto.CreateRequest request, String sender){
        return chatRoomRepository.existsByReceiverAndSender(sender, request.getTargetEmail());
    }

    @Transactional
    public void deleteRoom(String roomName, UserDetailsImpl userDetails){
        ChatRoom room = getRoom(roomName);
        String email = userDetails.getAccount().getEmail();

        if(!email.equals(room.getReceiver()) && !email.equals(room.getSender())){
            throw new RuntimeException("참여중인 채팅방만 삭제 가능합니다.");
        }
        room.changeStatus();
        chatRoomRepository.save(room);
    }

    private ChatRoom getRoom(String roomName){
        return chatRoomRepository.findChatRoomByRoomName(roomName)
                .orElseThrow(()->new RuntimeException("존재하지 않는 채팅방 입니다."));
    }
}
