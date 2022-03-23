package com.zerogreen.zerogreeneco.entity.community;

import com.zerogreen.zerogreeneco.entity.baseentity.BaseTimeEntity;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    private String replyContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replier_id")
    private BasicUser replier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private CommunityBoard board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reply_id", nullable = true)
    private BoardReply parentReply;

    @BatchSize(size = 1000)
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parentReply")
    private List<BoardReply> nestedReplyList = new ArrayList<>();

    private int depth = 1;

    public BoardReply(String replyContent, BasicUser replier, CommunityBoard board) {
        this.replyContent = replyContent;
        this.replier = replier;
        this.board = board;
    }

    @Builder(builderMethodName = "replyBuilder")
    public BoardReply(String replyContent, BasicUser replier, CommunityBoard board, List<BoardReply> nestedReplyList) {
        this.replyContent = replyContent;
        this.replier = replier;
        this.board = board;
        this.nestedReplyList = nestedReplyList;
    }

    public void changeText(String replyContent) {
        this.replyContent = replyContent;
    }

    public void addNestedReply(BoardReply nestedReply) {
        nestedReply.parentReply = this;
        nestedReply.depth = this.depth + 1;
        this.nestedReplyList.add(nestedReply);
    }

}
