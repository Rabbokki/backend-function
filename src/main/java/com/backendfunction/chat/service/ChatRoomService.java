package com.backendfunction.chat.service;

import com.backendfunction.account.entity.Account;
import com.backendfunction.account.repository.AccountRepository;
import com.backendfunction.chat.dto.ChatDto;
import com.backendfunction.chat.dto.RoomDto;
import com.backendfunction.chat.dto.UserResponseDto;
import com.backendfunction.chat.entity.ChatMessage;
import com.backendfunction.chat.entity.ChatRoom;
import com.backendfunction.chat.repository.ChatRoomRepository;
import com.backendfunction.global.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional
    public ChatRoom getChatRoom(String roomName, Account account) {
        ChatRoom room = getRoom(roomName);
        // 채팅 읽음 처리 (기존 로직 유지)
        room.getChats().stream()
                .filter(chat -> !chat.getSender().equals(account.getEmail()))
                .forEach(chatMessage -> chatMessage.setMessageCheckStatus(true));
        chatRoomRepository.save(room);
        return room;
    }

    private List<ChatDto.Response> chatConvertToResponseDto(List<ChatMessage> chats) {
        List<ChatDto.Response> responseList = new ArrayList<>();
        for (ChatMessage chat : chats) {
            ChatDto.Response response = ChatDto.Response.builder().chatMessage(chat).build();
            responseList.add(response);
        }
        return responseList;
    }

    private UserResponseDto getUserInfo(ChatRoom room, String account){
        UserResponseDto userResponseDto = new UserResponseDto();
        UserResponseDto.UserData userData = new UserResponseDto.UserData();

        String targetEmail = account.equals(room.getReceiver()) ? room.getSender() : room.getReceiver();

        Account targetAccount = accountRepository.findByEmail(targetEmail).orElseThrow(()->
                new RuntimeException("상대방 계정을 찾을 수 없습니다."));
        userData.setAccountId(targetAccount.getId());
        userData.setEmail(targetAccount.getEmail());
        userData.setNickname(targetAccount.getNickname());
        userData.setImgUrl(targetAccount.getImgUrl());
        userResponseDto.setData(userData);
        return userResponseDto;
    }

    public Page<RoomDto.Response> getChatRoomList(Account account, Pageable pageable) {
        String email = account.getEmail();
        log.info("Querying chat rooms for email: {}", email);
        Page<ChatRoom> rooms = chatRoomRepository.findAllByEmail(email, pageable);
        if (rooms.isEmpty()) {
            log.info("No chat rooms found for email: {}", email);
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        log.info("Found {} chat rooms for email: {}", rooms.getTotalElements(), email);
        return new PageImpl<>(entityToListDto(rooms, email), pageable, rooms.getTotalElements());
    }

    private List<RoomDto.Response> entityToListDto(Page<ChatRoom> rooms, String email) {
        return rooms.stream().map(room -> {
            try {
                return RoomDto.Response.builder()
                        .room(room)
                        .unreadMessageCount(getUnreadCount(room))
                        .latestChatMessage(getLatestChatMessage(room))
                        .userResponseDto(getUserInfo(room, email))
                        .build();
            } catch (Exception e) {
                log.error("Error converting room {}: {}", room.getRoomName(), e.getMessage());
                return null; // 예외 발생 시 null 반환 (필터링 필요)
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private String getLatestChatMessage(ChatRoom room) {
        Optional<ChatMessage> message = room.getChats().stream()
                .reduce((o1, o2) -> o1.getCreatedAt().isAfter(o2.getCreatedAt()) ? o1 : o2);
        return message.map(ChatMessage::getMessage).orElse(null);
    }

    private Long getUnreadCount(ChatRoom room) {
        return room.getChats().stream().filter(chat-> !chat.isMessageCheckStatus()).count();
    }

}
