package com.backendfunction.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatSimpleDto {
    public Long createdAt;
    public Long updatedAt;
    public String message;
    public byte message_check_status;
    public String sender;
    public Long roomId;
}
