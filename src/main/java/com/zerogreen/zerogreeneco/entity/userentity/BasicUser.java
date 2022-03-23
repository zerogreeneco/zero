package com.zerogreen.zerogreeneco.entity.userentity;

import com.zerogreen.zerogreeneco.entity.baseentity.BaseTimeEntity;
import com.zerogreen.zerogreeneco.entity.community.CommunityBoard;
import com.zerogreen.zerogreeneco.entity.detail.DetailReview;
import com.zerogreen.zerogreeneco.webSocket.entity.Notice;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "User_Type")
@DiscriminatorValue("ADMIN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class BasicUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;
    // 개인 연락처
    private String phoneNumber;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private boolean AuthState; // 이메일 인증 여부

    @OneToMany(mappedBy = "reviewer", cascade = REMOVE)
    List<DetailReview> detailReviewList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = REMOVE)
    List<CommunityBoard> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notice> notices;

    // 회원가입
    public BasicUser(String username, String phoneNumber,
                     String password, UserRole userRole, boolean authState) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userRole = userRole;
        AuthState = authState;
    }

    // Kakao 회원가입
    public BasicUser(String username, String password, UserRole userRole) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    // 수정용
    public BasicUser(String username, String phoneNumber, String password, UserRole userRole) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userRole = userRole;
    }

    // 수정용
    public BasicUser(String nickname, String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /*
     * Setter
     * */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setAuthState(boolean authState) {
        AuthState = authState;
    }

    //LikesService
    @Builder(builderMethodName = "likesBuilder")
    public BasicUser(String username, Long id) {
        super();
    }

}
