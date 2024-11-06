package com.service.chat.controller;

import com.service.chat.dto.CustomResult;
import com.service.chat.dto.response.RoomUserDto;
import com.service.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("get_chat_room")
    public ResponseEntity<CustomResult> getChatRoomByUser(@RequestParam Long userId){
        var customResult = chatService.getAllChatRoomOfUser(userId);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("get_room")
    public ResponseEntity<?> getChatRoom(@RequestParam Long roomId){
        var customResult = chatService.getChatRoomById(roomId);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("test_query")
    public ResponseEntity<?> test(@RequestParam Long roomId){
        var chatRoom = chatService.testOneToManyQuery(roomId);


        var newCustomResult = new CustomResult(200, "Success", chatRoom);

        return ResponseEntity.ok(newCustomResult);
    }
}
