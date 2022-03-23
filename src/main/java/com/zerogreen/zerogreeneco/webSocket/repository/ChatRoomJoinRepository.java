package com.zerogreen.zerogreeneco.webSocket.repository;

import org.springframework.data.repository.CrudRepository;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;
import com.zerogreen.zerogreeneco.webSocket.entity.ChatRoom;
import com.zerogreen.zerogreeneco.webSocket.entity.ChatRoomJoin;

import java.util.List;

public interface ChatRoomJoinRepository extends CrudRepository<ChatRoomJoin,Long> {
    List<ChatRoomJoin> findByUser(BasicUser user);

    List<ChatRoomJoin> findByChatRoom(ChatRoom chatRoom);

}
