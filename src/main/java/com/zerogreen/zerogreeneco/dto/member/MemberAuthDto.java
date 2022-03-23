package com.zerogreen.zerogreeneco.dto.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberAuthDto {

    @NotBlank
    private String username;

    @NotBlank
    private String nickname;

    public MemberAuthDto(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
}
