package com.service.chat.repository;

import com.service.chat.entity.ChatRoom;
import com.service.chat.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    List<ChatRoomUser> findALlByUserId(Long id);

    @Query(value = "select p.userId from ChatRoomUser p where p.chatRoom.id = ?1")
    List<Long> getUserByChatRoomId(Long id);

    @Query("SELECT cfu.userId " +
            "FROM ChatRoomUser cfu " +
            "WHERE cfu.chatRoom.id IN (" +
            "    SELECT cfu2.chatRoom.id " +
            "    FROM ChatRoomUser cfu2 " +
            "    GROUP BY cfu2.chatRoom.id " +
            "    HAVING COUNT(cfu2) = 2 AND SUM(CASE WHEN cfu2.userId = :userId THEN 1 ELSE 0 END) > 0" +
            ") " +
            "AND cfu.userId != :userId")
    List<Long> findAllFriends(@Param("userId") int userId);
}
