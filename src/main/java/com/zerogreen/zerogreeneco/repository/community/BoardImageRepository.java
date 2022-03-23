package com.zerogreen.zerogreeneco.repository.community;

import com.zerogreen.zerogreeneco.entity.community.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    // 게시글별 이미지 리스트
    @Query("SELECT image FROM BoardImage image WHERE image.board.id=:boardId ")
    List<BoardImage> findByBoard(@Param("boardId") Long boardId);

}
