package com.zerogreen.zerogreeneco.repository.community;

import com.zerogreen.zerogreeneco.entity.community.CommunityBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard, Long>, CommunityBoardRepositoryCustom {

    @Modifying
    @Transactional
    @Query("UPDATE CommunityBoard cb SET cb.count = cb.count + 1 WHERE cb.id=:boardId ")
    int boardCount(@Param("boardId") Long boardId);

}
