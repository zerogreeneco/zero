package com.zerogreen.zerogreeneco.entity.community;

import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommunityLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private CommunityBoard board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private BasicUser basicUser;

    public CommunityLike(CommunityBoard board, BasicUser basicUser) {
        this.board = board;
        this.basicUser = basicUser;
    }
}
