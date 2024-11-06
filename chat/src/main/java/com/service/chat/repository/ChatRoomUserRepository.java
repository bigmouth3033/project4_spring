package com.service.chat.repository;

import com.service.chat.entity.ChatRoom;
import com.service.chat.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    List<ChatRoomUser> findALlByUserId(Long id);

    @Query(value = "select p.userId from ChatRoomUser p where p.chatRoom.id = ?1")
    List<Long> getUserByChatRoomId(Long id);

}
