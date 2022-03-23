package com.zerogreen.zerogreeneco.security.dto;

import lombok.Getter;
import zerogreen.eco.entity.userentity.Member;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String nickname;
    private String username;

    public SessionUser(Member member) {
        this.nickname = member.getNickname();
        this.username = member.getUsername();
    }
}
