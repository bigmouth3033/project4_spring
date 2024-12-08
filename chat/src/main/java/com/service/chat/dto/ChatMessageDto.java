package com.service.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private String messageId;
    private Long senderId;
    private String senderAvatar;
    private String message;
    private String date;
    private Long roomId;
    private String type = "MESSAGE";
}
