package com.backendfunction.chat.controller;

import com.backendfunction.chat.dto.ChatDto;
import com.backendfunction.chat.dto.RoomDto;
import com.backendfunction.chat.entity.ChatRoom;
import com.backendfunction.chat.service.ChatRoomService;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.user.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping
    @Transactional
    public ResponseDto<ChatDto.CreateResponse> createRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @Valid @RequestBody RoomDto.CreateRequest request) {
        log.info("Creating room for account: id={}, email={}",
                userDetails.getAccount().getId(), userDetails.getAccount().getEmail());
        ChatDto.CreateResponse response = chatRoomService.createRoom(request, userDetails);
        return ResponseDto.success(response);
    }

    @PostMapping("/{roomName}")
    public ResponseDto<?> deleteRoom(@PathVariable("roomName") String roomName,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails){
        chatRoomService.deleteRoom(roomName,userDetails);
        return ResponseDto.success("삭제 성공");
    }

    @GetMapping("/{roomName}")
    public ResponseDto<?> getChatRoomDetail(@PathVariable("roomName") String roomName,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        ChatRoom room = chatRoomService.getChatRoom(roomName,userDetails.getAccount());
        ChatDto.CreateResponse response = ChatDto.CreateResponse.builder()
                .id(room.getId())
                .name(room.getRoomName())
                .build();
        return ResponseDto.success(response);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<RoomDto.Response>> getChatRoomList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault Pageable pageable) {
        if (userDetails == null || userDetails.getAccount() == null) {
            log.error("UserDetails is null - Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new PageImpl<>(Collections.emptyList(), pageable, 0));
        }
        log.info("Fetching chat room list for email: {}", userDetails.getAccount().getEmail());
        return ResponseEntity.ok(chatRoomService.getChatRoomList(userDetails.getAccount(), pageable));
    }

}
