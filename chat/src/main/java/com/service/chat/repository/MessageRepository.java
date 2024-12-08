package com.service.chat.repository;

import com.service.chat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT m FROM Message m WHERE m.chatRoom.id = :chatRoomId ORDER BY m.id DESC limit  1")
    Message findLastMessageByChatRoomId(Long chatRoomId);

    @Query(value = "select c from Message c where c.chatRoom.id = :chatRoomId")
    Page<Message> findAllByChatRoomId(@Param("chatRoomId") int chatRoomId, Pageable pageable);
}
