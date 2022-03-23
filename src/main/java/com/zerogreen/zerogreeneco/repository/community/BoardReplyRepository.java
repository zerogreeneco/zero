package com.zerogreen.zerogreeneco.repository.community;

import com.zerogreen.zerogreeneco.entity.community.BoardReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

    @Query("SELECT reply FROM BoardReply reply " +
            " JOIN FETCH reply.board board " +
            " JOIN FETCH reply.replier replier " +
            " WHERE reply.board.id=:boardId " +
            " ORDER BY COALESCE(reply.parentReply.id, reply.id), reply.depth, reply.createdDate asc ")
    List<BoardReply> findBoardRepliesByBoardId(@Param("boardId") Long boardId);
}
