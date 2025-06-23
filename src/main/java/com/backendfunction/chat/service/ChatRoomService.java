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
        String sender = userDetails.getAccount().getEmail();
        String receiver = request.getTargetEmail();
        Long postId = request.getPostId();

        if (sender.equals(receiver)) {
            throw new IllegalArgumentException("자신과 채팅할 수 없습니다.");
        }

        // postId로 기존 방 확인
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByPostIdAndSenderOrReceiver(postId, sender, receiver);
        if (existingRoom.isPresent()) {
            ChatRoom room = existingRoom.get();
            return ChatDto.CreateResponse.builder().id(room.getId()).name(room.getRoomName()).build();
        }

        String roomName = postId + "_" + sender; // 또는 고정된 형식으로 변경 가능
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(roomName);
        chatRoom.setPostId(postId);
        chatRoom.setSender(sender);
        chatRoom.setReceiver(receiver);
        chatRoom = chatRoomRepository.save(chatRoom);
        return ChatDto.CreateResponse.builder().id(chatRoom.getId()).name(chatRoom.getRoomName()).build();
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

    private ChatRoom getRoom(String roomName) {
        log.info("Fetching room with roomName: {}", roomName);
        Optional<ChatRoom> room = chatRoomRepository.findChatRoomByRoomName(roomName);
        if (room.isEmpty()) {
            log.warn("No chat room found for roomName: {}", roomName);
        }
        return room.orElseThrow(() -> new RuntimeException("존재하지 않는 채팅방 입니다."));
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

    public List<ChatDto.Response> chatConvertToResponseDto(List<ChatMessage> chats) {
        List<ChatDto.Response> responseList = new ArrayList<>();
        for (ChatMessage chat : chats) {
            // 예시: roomId, roomName, timestamp는 ChatMessage에서 꺼내거나 가공해서 넘겨야 함
            ChatDto.Response response = ChatDto.Response.fromChatMessage(
                    chat,
                    chat.getRoom().getId(),           // ChatMessage에 해당 메서드가 있어야 함
                    chat.getRoom().getRoomName(),         // 마찬가지로 getter 필요
                    chat.getCreatedAt().toString()  // 예시로 timestamp 처리
            );
            responseList.add(response);
        }
        return responseList;
    }


    private UserResponseDto getUserInfo(ChatRoom room, String account) {
        UserResponseDto userResponseDto = new UserResponseDto();
        UserResponseDto.UserData userData = new UserResponseDto.UserData();
        String targetEmail = account.equals(room.getReceiver()) ? room.getSender() : room.getReceiver();
        log.debug("Fetching user info for targetEmail: {}", targetEmail);
        Account targetAccount = accountRepository.findByEmail(targetEmail).orElse(null);
        if (targetAccount == null) {
            log.warn("No account found for email: {}", targetEmail);
            userData.setEmail(targetEmail);
            userData.setNickname("Unknown");
            userData.setAccountId(0L);
        } else {
            userData.setAccountId(targetAccount.getId());
            userData.setEmail(targetAccount.getEmail());
            userData.setNickname(targetAccount.getNickname());
            userData.setImgUrl(targetAccount.getImgUrl());
        }
        userResponseDto.setData(userData);
        return userResponseDto;
    }

    public Page<RoomDto.Response> getChatRoomList(Account account, Pageable pageable) {
        String email = account.getEmail();
        log.info("Querying chat rooms for email: {}", email);
        Page<ChatRoom> rooms = chatRoomRepository.findAllByEmail(email, pageable);
        log.info("Found {} rooms for email: {}", rooms.getTotalElements(), email);
        rooms.forEach(room -> log.debug("Room: id={}, name={}, sender={}, receiver={}",
                room.getId(), room.getRoomName(), room.getSender(), room.getReceiver()));
        if (rooms.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        return new PageImpl<>(entityToListDto(rooms, email), pageable, rooms.getTotalElements());
    }

    private List<RoomDto.Response> entityToListDto(Page<ChatRoom> rooms, String email) {
        return rooms.stream()
                .map(room -> {
                    try {
                        log.debug("Processing room: id={}, name={}, sender={}, receiver={}",
                                room.getId(), room.getRoomName(), room.getSender(), room.getReceiver());
                        RoomDto.Response response = RoomDto.Response.builder()
                                .room(room)
                                .unreadMessageCount(getUnreadCount(room))
                                .latestChatMessage(getLatestChatMessage(room))
                                .userResponseDto(getUserInfo(room, email))
                                .build();
                        return response;
                    } catch (Exception e) {
                        log.error("Error converting room {}: {}", room.getRoomName(), e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String getLatestChatMessage(ChatRoom room) {
        Optional<ChatMessage> message = room.getChats().stream()
                .reduce((o1, o2) -> o1.getCreatedAt().isAfter(o2.getCreatedAt()) ? o1 : o2);
        return message.map(ChatMessage::getMessage).orElse(null);
    }

    private Long getUnreadCount(ChatRoom room) {
        return room.getChats().stream().filter(chat-> !chat.isMessageCheckStatus()).count();
    }

    // 테스트 메서드 추가
    @Transactional(readOnly = true)
    public Page<ChatRoom> testChatRoomsForWnsdyd() {
        Page<ChatRoom> rooms = chatRoomRepository.findAllByEmail("wnsdyd821@gmail.com", Pageable.unpaged());
        rooms.forEach(room -> log.info("Room for wnsdyd821@gmail.com: id={}, name={}, sender={}, receiver={}",
                room.getId(), room.getRoomName(), room.getSender(), room.getReceiver()));
        return rooms; // Page<ChatRoom> 반환
    }

}
