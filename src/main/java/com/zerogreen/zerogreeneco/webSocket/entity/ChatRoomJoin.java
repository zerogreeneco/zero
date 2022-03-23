package com.zerogreen.zerogreeneco.webSocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ChatRoomJoin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name =  "user_id", nullable = false)
    private BasicUser user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    public ChatRoomJoin(BasicUser user , ChatRoom chatRoom){
        this.user=user;
        this.chatRoom=chatRoom;
    }
}
