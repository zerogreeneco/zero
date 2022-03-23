package com.zerogreen.zerogreeneco.webSocket.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import zerogreen.eco.entity.userentity.BasicUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Notice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String link;
    @ManyToOne
    private BasicUser user;
    private LocalDateTime time;

    public Notice(BasicUser user, String content, String link, LocalDateTime time){
        this.user=user;
        this.content = content;
        this.link=link;
        this.time = time;
    }
}
