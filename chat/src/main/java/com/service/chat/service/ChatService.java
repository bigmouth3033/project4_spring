package com.service.chat.service;

import com.service.chat.dto.ChatMessageDto;
import com.service.chat.dto.ChatRoomDto;
import com.service.chat.dto.ChatRoomUserDto;
import com.service.chat.dto.CustomResult;
import com.service.chat.dto.response.RoomListDto;
import com.service.chat.dto.response.RoomUserDto;
import com.service.chat.entity.ChatRoom;
import com.service.chat.entity.ChatRoomUser;
import com.service.chat.entity.Message;
import com.service.chat.repository.ChatRoomRepository;
import com.service.chat.repository.ChatRoomUserRepository;
import com.service.chat.repository.MessageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomUserRepository chatRoomUserRepository;

    private final MessageRepository messageRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatRoomUserRepository chatRoomUserRepository, MessageRepository messageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomUserRepository = chatRoomUserRepository;
        this.messageRepository = messageRepository;
    }

    public List<Long> getChatRoomById(Long id) {
        return chatRoomRepository.findChatRoomWithUsers(id);
    }

    public Object testOneToManyQuery(Long id) {
        ChatRoom result = chatRoomRepository.testQueryOneToMany(id);
//        var chatRoomDto = new ChatRoomDto();
//        BeanUtils.copyProperties(result, chatRoomDto);
//        chatRoomDto.setMessages(null);
//        List<ChatRoomUserDto> chatRoomUserDtoList =  result.getRoomUsers().stream().map(u -> {
//            ChatRoomUserDto chatRoomUserDto = new ChatRoomUserDto();
//            chatRoomUserDto.setId(u.getId());
//            chatRoomUserDto.setUserId(u.getUserId());
//            return chatRoomUserDto;
//        }).toList();
//
//        chatRoomDto.setRoomUsers(chatRoomUserDtoList);

        return result.getRoomUsers();
    }

    public CustomResult getAllChatRoomOfUser(Long id) {
        try {
            var chatRoom = chatRoomUserRepository.findALlByUserId(id);

            var listRoom = new ArrayList<RoomListDto>();

            for (ChatRoomUser room : chatRoom) {
                var newRoom = new RoomListDto();
                newRoom.setRoomId(room.getChatRoom().getId());
                newRoom.setUserIDs(chatRoomUserRepository.getUserByChatRoomId(room.getChatRoom().getId()));
                listRoom.add(newRoom);
            }

            return new CustomResult(200, "Success", listRoom);

        } catch (Exception e) {
            return new CustomResult(400, "Error", e.getMessage());
        }
    }

    public void saveUserMessage(ChatMessageDto chatMessageDto) {
        try {
            var newMessage = new Message();
            newMessage.setCreatedAt(new Date());
            newMessage.setMessage(chatMessageDto.getMessage());
            newMessage.setSenderId(chatMessageDto.getSenderId());
            var chatRoom = chatRoomRepository.findChatRoomById(chatMessageDto.getRoomId());
            newMessage.setChatRoom(chatRoom);

            messageRepository.save(newMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
