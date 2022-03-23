package com.zerogreen.zerogreeneco.dto.member;

import com.zerogreen.zerogreeneco.entity.userentity.Member;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;
import com.zerogreen.zerogreeneco.entity.userentity.VegetarianGrade;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MemberJoinDto {

    @NotBlank
    @Email // @ 없으면 에러 발생 마지막 .com 등이 빠져도 로직 진행 Ok
    private String username;

    @NotBlank
    private String nickname;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,12}", message = "숫자,소문자,대문자,특수기호 포함 8~12글자")
    private String password;

    private VegetarianGrade vegetarianGrade;

    public Member toMember(MemberJoinDto memberJoinDto) {
        return new Member(memberJoinDto.username, memberJoinDto.nickname,
                memberJoinDto.phoneNumber, memberJoinDto.password, UserRole.USER, false, memberJoinDto.vegetarianGrade);
    }
}
