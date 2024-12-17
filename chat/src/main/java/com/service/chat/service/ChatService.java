package com.service.chat.service;

import com.service.chat.dto.AddGroupDto;
import com.service.chat.dto.ChatMessageDto;
import com.service.chat.dto.CustomPaging;
import com.service.chat.dto.CustomResult;
import com.service.chat.dto.response.RoomListDto;
import com.service.chat.dto.response.UserDto;
import com.service.chat.entity.ChatRoom;
import com.service.chat.entity.ChatRoomUser;
import com.service.chat.entity.Message;
import com.service.chat.repository.ChatRoomRepository;
import com.service.chat.repository.ChatRoomUserRepository;
import com.service.chat.repository.MessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ChatService {


    private final RestClient restClient;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomUserRepository chatRoomUserRepository;

    private final MessageRepository messageRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatRoomUserRepository chatRoomUserRepository, MessageRepository messageRepository, RestClient restClient) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomUserRepository = chatRoomUserRepository;
        this.messageRepository = messageRepository;
        this.restClient = restClient;
    }

    public CustomResult addNewFriend(int userId, int friendId) {
        try{
            var listFriend = chatRoomUserRepository.findAllFriends(userId);

            if(listFriend.contains((long) friendId)){
                return new CustomResult(403, "Already friend", null);
            }

            var newChatGroup = new ChatRoom();
            chatRoomRepository.save(newChatGroup);
            var newRoomUser1 = new ChatRoomUser();
            newRoomUser1.setChatRoom(newChatGroup);
            newRoomUser1.setUserId((long) userId);
            var newRoomUser2 = new ChatRoomUser();
            newRoomUser2.setChatRoom(newChatGroup);
            newRoomUser2.setUserId((long) friendId);
            chatRoomUserRepository.save(newRoomUser1);
            chatRoomUserRepository.save(newRoomUser2);
            return new CustomResult(200, "Success", newChatGroup.getId());
        }catch (Exception e){
            return new CustomResult(400, "Bad request", null);
        }
    }

    public List<Long> getChatRoomById(Long id) {
        return chatRoomRepository.findChatRoomWithUsers(id);
    }

    public CustomResult searchForNewFriend(int userId, String search) {
        try {
            var listFriend = chatRoomUserRepository.findAllFriends(userId);

            var result = restClient.get().uri(uriBuilder ->
                    uriBuilder.path("/userCM/search_user_chat")
                            .queryParam("search", search)
                            .queryParam("userId", userId)
                            .queryParam("friendsId", listFriend).build())
                    .retrieve()
                    .body(List.class);

            return new CustomResult(200, "Success", result);

        } catch (Exception e) {
            return new CustomResult(400, "Bad request", e.getMessage());
        }
    }

    public CustomResult getAllChatRoomOfUser(Long id) {
        try {
            var chatRoom = chatRoomUserRepository.findALlByUserId(id);

            var listRoom = new ArrayList<RoomListDto>();

            for (ChatRoomUser room : chatRoom) {
                var newRoom = new RoomListDto();

                newRoom.setName(room.getChatRoom().getName());

                newRoom.setRoomId(room.getChatRoom().getId());
                var idLists = chatRoomUserRepository.getUserByChatRoomId(room.getChatRoom().getId());

                List<UserDto> listUser = new ArrayList<>();
                for (var uid : idLists) {
                    var userInfo = restClient.get().uri("/userCM/get_user_info_by_id/" + uid).retrieve().body(UserDto.class);
                    if(userInfo != null){
                        listUser.add(userInfo);
                    }else{
                        var admin =new UserDto();
                        admin.setId(0);
                        admin.setFirstName("UrbanNest");
                        admin.setLastName("Admin");
                        admin.setAvatar( "https://firebasestorage.googleapis.com/v0/b/eproject4-3c13d.appspot.com/o/8fa6a3e3-263f-4be9-bc42-5d0c9f2c6175.png?alt=media");
                        listUser.add(admin);
                    }
                }
                newRoom.setUsers(listUser);
                var message = messageRepository.findLastMessageByChatRoomId(room.getChatRoom().getId());
                if (message != null) {
                    newRoom.setLastMessage(message.getMessage());
                    newRoom.setLastestMessageDate(message.getCreatedAt());
                }else{
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(2000, Calendar.JANUARY, 1); // January 1, 2000
                    newRoom.setLastestMessageDate(calendar.getTime());
                }

                listRoom.add(newRoom);
            }

            listRoom.sort((r1, r2) -> r2.getLastestMessageDate().compareTo(r1.getLastestMessageDate()));

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

    public CustomPaging getRoomMessage(int pageNumber, int pageSize, int roomId) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc("id")));
            var messages = messageRepository.findAllByChatRoomId(roomId, pageable);

            var customPaging = new CustomPaging();
            customPaging.setStatus(200);
            customPaging.setMessage("Success");
            customPaging.setCurrentPage(pageNumber);
            customPaging.setPageSize(pageSize);
            customPaging.setTotalPages(messages.getTotalPages());
            customPaging.setTotalCount(messages.getTotalElements());
            customPaging.setData(messages.getContent());

            return customPaging;
        } catch (Exception e) {
            var customPaging = new CustomPaging();
            customPaging.setMessage(e.getMessage());
            return customPaging;
        }
    }

    public CustomResult createNewGroupChat(AddGroupDto addGroupDto) {
        try{
            var newGroup = new ChatRoom();
            newGroup.setName(addGroupDto.getGroupName());
            chatRoomRepository.save(newGroup);

            for(var member : addGroupDto.getMembers()) {
                var newMember = new ChatRoomUser();
                newMember.setChatRoom(newGroup);
                newMember.setUserId((long) member);
                chatRoomUserRepository.save(newMember);
            }

            return new CustomResult(200, "Success", newGroup.getId());

        }catch (Exception e){
            return new CustomResult(400, "Bad request", null);
        }
    }
}
