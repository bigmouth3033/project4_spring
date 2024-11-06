package com.service.chat.controller;


import com.service.chat.dto.ChatMessageDto;
import com.service.chat.entity.ChatRoomUser;
import com.service.chat.service.ChatService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;


@Controller
public class ChatSocketController {
    private static final Logger log = LogManager.getLogger(ChatSocketController.class);
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatService chatService;


    @MessageMapping("/private-message")
    public ChatMessageDto recMessage(@Payload ChatMessageDto message) {
        var roomUsers = chatService.getChatRoomById(message.getRoomId());

        if (roomUsers != null) {
            for (Long id : roomUsers) {
                if(!Objects.equals(id, message.getSenderId())){
                    simpMessagingTemplate.convertAndSendToUser(String.valueOf(id), "/private", message);
                }
            }

            chatService.saveUserMessage(message);
        }

        System.out.println(message.toString());
        return message;
    }

}
