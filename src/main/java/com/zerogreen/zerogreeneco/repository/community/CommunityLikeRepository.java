package com.zerogreen.zerogreeneco.repository.community;

import com.zerogreen.zerogreeneco.entity.community.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {

    @Modifying
    @Query("DELETE FROM CommunityLike c WHERE c.board.id=:boardId AND c.basicUser.id=:memberId ")
    void deleteLike(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    @Query("SELECT COUNT(c.id) FROM CommunityLike c WHERE c.board.id=:boardId AND c.basicUser.id=:memberId ")
    int countLike(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    @Query("SELECT COUNT(c.id) FROM CommunityLike c WHERE c.board.id=:boardId ")
    int countByBoard(@Param("boardId") Long boardId);

}

