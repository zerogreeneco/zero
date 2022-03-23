package com.zerogreen.zerogreeneco.service.community;


import com.zerogreen.zerogreeneco.dto.community.CommunityReplyDto;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;

import java.util.List;

public interface CommunityReplyService {

    void replySave(String text, Long boardId, BasicUser basicUser);
    void nestedReplySave(String text, Long boardId, BasicUser basicUser, Long replyId);

    void modifyReply(Long replyId, String text);

    void deleteReply(Long replyId);

    List<CommunityReplyDto> findReplyByBoardId(Long boardId);
}
