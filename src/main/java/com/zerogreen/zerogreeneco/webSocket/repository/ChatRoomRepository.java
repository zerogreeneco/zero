package com.zerogreen.zerogreeneco.webSocket.repository;


import org.springframework.data.repository.CrudRepository;
import com.zerogreen.zerogreeneco.webSocket.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom,Long> {
    Optional<ChatRoom> findById(Long id);
}
