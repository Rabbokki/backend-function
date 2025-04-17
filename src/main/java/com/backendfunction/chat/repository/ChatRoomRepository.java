package com.backendfunction.chat.repository;

import com.backendfunction.chat.entity.ChatMessage;
import com.backendfunction.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    Optional<ChatRoom> findChatRoomByRoomName(String roomName);

    @Query("SELECT r FROM ChatRoom r WHERE r.sender = :email OR r.receiver = :email " +
            "ORDER BY r.updatedAt DESC")
    Page<ChatRoom> findAllByEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT r FROM ChatRoom r WHERE r.postId = :postId AND r.sender = :sender")
    Optional<ChatRoom> findByPostIdAndSender(@Param("postId") Long postId, @Param("sender") String sender);


    @Query("SELECT COUNT(r) > 0 FROM ChatRoom r "
            + "WHERE (r.receiver = :receiver AND r.sender = :sender) "
            + "OR r.sender = :receiver AND r.receiver = :sender")
    boolean existsByReceiverAndSender(@Param("receiver") String receiver,
                                      @Param("sender") String sender);

    @Query("SELECT r FROM ChatRoom r " + "WHERE (r.receiver = :receiver AND r.sender = :sender) "
            + "OR r.sender = :receiver AND r.receiver = :sender")
    ChatRoom findByReceiverAndSender(@Param("receiver") String receiver,
                                     @Param("sender") String sender);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.postId = :postId AND " +
            "((cr.sender = :sender AND cr.receiver = :receiver) OR (cr.sender = :receiver AND cr.receiver = :sender))")
    Optional<ChatRoom> findByPostIdAndSenderOrReceiver(@Param("postId") Long postId,
                                                       @Param("sender") String sender,
                                                       @Param("receiver") String receiver);
}
