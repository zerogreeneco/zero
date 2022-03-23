package com.zerogreen.zerogreeneco.dto.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class FindMemberDto {

//    @NotBlank(message = "비밀번호를 찾을 아이디를 입력해주세요.")
    @Email
    private String username;

//    @NotBlank(message = "계정과 연결된 연락처를 입력해주세요.")
    private String phoneNumber;

    public FindMemberDto() {}

    public FindMemberDto(String username) {
        this.username = username;
    }

    public FindMemberDto(String username, String phoneNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
    }
}
