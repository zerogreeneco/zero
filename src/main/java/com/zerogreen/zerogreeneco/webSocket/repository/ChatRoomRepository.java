package com.zerogreen.zerogreeneco.webSocket.repository;


import org.springframework.data.repository.CrudRepository;
import zerogreen.eco.webSocket.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom,Long> {
    Optional<ChatRoom> findById(Long id);
}
