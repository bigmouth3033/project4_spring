package com.service.chat.controller;

import com.service.chat.dto.AddFriend;
import com.service.chat.dto.AddGroupDto;
import com.service.chat.dto.CustomPaging;
import com.service.chat.dto.CustomResult;
import com.service.chat.dto.response.RoomUserDto;
import com.service.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("get_room_messages")
    public ResponseEntity<CustomPaging> getChatRoomMessages(@RequestParam int roomId, @RequestParam int pageNumber, @RequestParam int pageSize){
        var customPaging = chatService.getRoomMessage(pageNumber, pageSize, roomId);
        return ResponseEntity.ok(customPaging);
    }

    @GetMapping("search_new_frient")
    public ResponseEntity<CustomResult> searchNewFriend(@RequestParam int userId, @RequestParam(required = false, defaultValue = "") String search){
        var customResult = chatService.searchForNewFriend(userId, search);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping("add_new_friend")
    public ResponseEntity<CustomResult> addNewFriend(@ModelAttribute AddFriend addFriend){
        var customResult = chatService.addNewFriend(addFriend.getUserId(),addFriend.getFriendId());
        return ResponseEntity.ok(customResult);
    }

    @PostMapping("add_new_group")
    public ResponseEntity<CustomResult> addNewGroup(@ModelAttribute AddGroupDto addGroupDto){
        var customResult = chatService.createNewGroupChat(addGroupDto);
        return ResponseEntity.ok(customResult);
    }

}
