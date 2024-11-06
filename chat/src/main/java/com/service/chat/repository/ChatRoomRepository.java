package com.service.chat.repository;

import com.service.chat.dto.response.RoomUserDto;
import com.service.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findChatRoomById(Long id);

//    @Query(value = "SELECT p.user_id FROM chat_room u LEFT JOIN chat_room_user p ON u.id = p.chat_room_id WHERE u.id = :chatRoomId", nativeQuery = true)
    @Query(value = "select p.userId from ChatRoomUser p where p.chatRoom.id = :chatRoomId")
    List<Long> findChatRoomWithUsers(@Param("chatRoomId") Long chatRoomId);


    @Query(value = "select p from ChatRoom p where p.id = :chatRoomId")
    ChatRoom testQueryOneToMany(@Param("chatRoomId") Long chatRoomId);

}
