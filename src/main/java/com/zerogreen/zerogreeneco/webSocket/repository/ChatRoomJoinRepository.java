package com.zerogreen.zerogreeneco.webSocket.repository;

import org.springframework.data.repository.CrudRepository;
import zerogreen.eco.entity.userentity.BasicUser;
import zerogreen.eco.webSocket.entity.ChatRoom;
import zerogreen.eco.webSocket.entity.ChatRoomJoin;

import java.util.List;

public interface ChatRoomJoinRepository extends CrudRepository<ChatRoomJoin,Long> {
    List<ChatRoomJoin> findByUser(BasicUser user);

    List<ChatRoomJoin> findByChatRoom(ChatRoom chatRoom);

}
