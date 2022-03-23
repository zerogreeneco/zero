package com.zerogreen.zerogreeneco.entity.userentity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@DiscriminatorValue("MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@OnDelete(action = OnDeleteAction.CASCADE)
@ToString
public class Member extends BasicUser {

    @Enumerated(EnumType.STRING)
    private VegetarianGrade vegetarianGrade;
    private String nickname;

    private String socialType;


    // 회원 가입용
    @Builder
    public Member(String username, String nickname, String phoneNumber, String password,
                  UserRole userRole,Boolean authState,VegetarianGrade vegetarianGrade) {
        super(username, phoneNumber, password, userRole, authState);
        this.vegetarianGrade = vegetarianGrade;
        this.nickname = nickname;
    }

    // OAUTH 회원가입
    @Builder(builderMethodName = "OauthRegister", buildMethodName = "oauthRegister")
    public Member(String username, String nickname, String phoneNumber, String password,
                  UserRole userRole, Boolean authState, VegetarianGrade vegetarianGrade, String socialType) {
        super(username, phoneNumber, password, userRole, authState);
        this.vegetarianGrade = vegetarianGrade;
        this.nickname = nickname;
        this.socialType = socialType;
    }

    @Builder(builderMethodName = "googleBuilder")
    public Member(String username, String password, UserRole userRole, String nickname) {
        super(username, password, userRole);
        this.nickname = nickname;
    }

    // 수정용
    public Member(String nickname, String phoneNumber, VegetarianGrade vegetarianGrade) {
        super(nickname, phoneNumber);
        this.vegetarianGrade = vegetarianGrade;
    }

    // 테스트용
    public Member(String username, String nickname, String phoneNumber, String password, UserRole userRole, VegetarianGrade vegetarianGrade) {
        super(username, phoneNumber, password, userRole);
        this.vegetarianGrade = vegetarianGrade;
        this.nickname = nickname;
    }

    //Review
    public Member(String nickname) {
    }

    // oauth update
    public Member update(String nickname, String socialType) {
        this.nickname = nickname;
        this.socialType = socialType;
        return this;
    }
}
