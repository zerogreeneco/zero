package com.zerogreen.zerogreeneco.webSocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private BasicUser writer;

    public ChatMessage(String message, LocalDateTime time, ChatRoom chatRoom, BasicUser writer){
        this.message=message;
        this.time=time;
        this.chatRoom=chatRoom;
        this.writer=writer;
    }
}
