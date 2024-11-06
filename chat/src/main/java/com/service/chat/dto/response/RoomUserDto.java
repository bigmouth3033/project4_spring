package com.service.chat.dto.response;

import com.service.chat.entity.ChatRoomUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomUserDto {
    private Long id;
    private String name;
    private List<ChatRoomUser> users;
}
