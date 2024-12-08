package com.service.chat.dto.response;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomListDto {
    private Long roomId;
    private String name;
    private List<UserDto> users;
    private String lastMessage;
    private Date lastestMessageDate;

}
