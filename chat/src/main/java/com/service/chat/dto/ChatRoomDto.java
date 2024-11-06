package com.service.chat.dto;

import com.service.chat.entity.ChatRoom;
import com.service.chat.entity.ChatRoomUser;
import com.service.chat.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {

    private Long id;

    private String name;

    private List<Message> messages;

    private List<ChatRoomUserDto> roomUsers;

    private Date createdAt;

    private Date updatedAt;


}
