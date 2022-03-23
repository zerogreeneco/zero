package com.zerogreen.zerogreeneco.dto.community;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerogreen.zerogreeneco.entity.community.BoardReply;
import com.zerogreen.zerogreeneco.entity.userentity.Member;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;
import com.zerogreen.zerogreeneco.entity.userentity.VegetarianGrade;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Slf4j
public class CommunityReplyDto {

    private Long replyId;
    private Long boardId;
    private Long parentReplyId;
    private Long memberId;
    private int depth;
    private String text;
    private String nickname;
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    private VegetarianGrade vegetarianGrade;

    public CommunityReplyDto() {
    }

    // 댓글
    public CommunityReplyDto(BoardReply boardReply) {

        // 멤버 타입에 따라서 nickname 분기
        if (boardReply.getReplier() instanceof Member) {
            this.nickname = ((Member) boardReply.getReplier()).getNickname();
            this.vegetarianGrade = ((Member) boardReply.getReplier()).getVegetarianGrade();
        } else if (boardReply.getReplier() instanceof StoreMember) {
            this.nickname = ((StoreMember) boardReply.getReplier()).getStoreName();
        } else if (boardReply.getReplier().getUserRole().equals(UserRole.ADMIN)) {
            this.nickname = boardReply.getReplier().getUsername();
        }

        this.replyId = boardReply.getId();
        this.boardId = boardReply.getBoard().getId();
        this.memberId = boardReply.getReplier().getId();
        this.text = boardReply.getReplyContent();
        this.username = boardReply.getReplier().getUsername();
        this.createdTime = boardReply.getModifiedDate();
        this.depth = boardReply.getDepth();
    }
}
