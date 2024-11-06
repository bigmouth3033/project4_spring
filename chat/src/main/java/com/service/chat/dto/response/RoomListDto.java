package com.service.chat.dto.response;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomListDto {
    private Long roomId;
    private List<Long> userIDs;
}
