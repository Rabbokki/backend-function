package com.backendfunction.chat.entity;

import com.backendfunction.account.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Entity
@CrossOrigin("*")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomName;
    private String sender;
    private String receiver;
    private boolean isDeleted;

    @OneToMany(mappedBy = "room",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<ChatMessage> chats;

    private String lastChatMessage;

    public void changeStatus(){
        this.isDeleted = true;
    }
}
