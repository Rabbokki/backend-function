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
            if (chatRoom.getRoomName() == null) {
                log.warn("Generated roomName is null, setting default value");
                chatRoom.setRoomName(UUID.randomUUID().toString()); // 기본값 설정
            }
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

    public List<ChatDto.Response> chatConvertToResponseDto(List<ChatMessage> chats) {
        List<ChatDto.Response> responseList = new ArrayList<>();
        for (ChatMessage chat : chats) {
            ChatDto.Response response = ChatDto.Response.builder().chatMessage(chat).build();
            responseList.add(response);
        }
        return responseList;
    }

    private UserResponseDto getUserInfo(ChatRoom room, String account) {
        UserResponseDto userResponseDto = new UserResponseDto();
        UserResponseDto.UserData userData = new UserResponseDto.UserData();
        String targetEmail = account.equals(room.getReceiver()) ? room.getSender() : room.getReceiver();

        try {
            Account targetAccount = accountRepository.findByEmail(targetEmail).orElse(null);
            if (targetAccount == null) {
                log.warn("Target account not found for email: {}", targetEmail);
                // UUID 형식인지 확인
                if (targetEmail.matches("^[0-9a-fA-F-]{36}$")) {
                    log.warn("Value {} appears to be a UUID, not an email. Expected an email for Account lookup.", targetEmail);
                    userData.setEmail(targetEmail);
                    userData.setNickname("Unknown (UUID)");
                    userData.setAccountId(0L);
                    // 참고: Account.id가 Long이므로 UUID를 직접 조회 불가.
                    // 만약 sender/receiver가 Account.id로 변경된다면 아래 주석 해제
                /*
                try {
                    Long accountId = convertUuidToAccountId(targetEmail); // 별도 메서드 필요
                    targetAccount = accountRepository.findById(accountId).orElse(null);
                    if (targetAccount != null) {
                        userData.setAccountId(targetAccount.getId());
                        userData.setEmail(targetAccount.getEmail());
                        userData.setNickname(targetAccount.getNickname());
                        userData.setImgUrl(targetAccount.getImgUrl());
                    }
                } catch (Exception e) {
                    log.error("Failed to convert UUID {} to account ID: {}", targetEmail, e.getMessage());
                }
                */
                } else {
                    userData.setEmail(targetEmail);
                    userData.setNickname("Unknown");
                    userData.setAccountId(0L);
                }
            } else {
                userData.setAccountId(targetAccount.getId());
                userData.setEmail(targetAccount.getEmail());
                userData.setNickname(targetAccount.getNickname());
                userData.setImgUrl(targetAccount.getImgUrl());
            }
        } catch (Exception e) {
            log.error("Error fetching user info for email {}: {}", targetEmail, e.getMessage());
            userData.setEmail(targetEmail);
            userData.setNickname("Error");
            userData.setAccountId(0L);
        }
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
                        log.debug("Built response: roomName={}, userEmail={}",
                                response.getRoomName(), response.getUserData() != null ? response.getUserData().getEmail() : "null");
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

}
