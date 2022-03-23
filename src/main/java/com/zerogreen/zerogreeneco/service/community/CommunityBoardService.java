package com.zerogreen.zerogreeneco.service.community;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import zerogreen.eco.dto.community.CommunityRequestDto;
import zerogreen.eco.dto.community.CommunityResponseDto;
import zerogreen.eco.dto.search.SearchCondition;
import zerogreen.eco.entity.community.BoardImage;
import zerogreen.eco.entity.community.Category;
import zerogreen.eco.entity.userentity.BasicUser;
import zerogreen.eco.entity.userentity.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CommunityBoardService {

    /* Community */
    Long boardRegister(CommunityRequestDto dto, Member writer, List<BoardImage> imageList);

    Slice<CommunityResponseDto> findAllCommunityBoard(Pageable pageable, SearchCondition condition);
    Slice<CommunityResponseDto> findAllCommunityBoard(Pageable pageable);
    Slice<CommunityResponseDto> findByCategory(Pageable pageable, Category category);

    CommunityResponseDto findDetailView(Long boardId, HttpServletRequest request, HttpServletResponse response);
    CommunityRequestDto boardModifyRequest(Long boardId);

    void boardModify(Long boardId, Category category, String text);
    void boardDelete(Long boardId);
    int boardCount(Long boardId);

    /* Like */
    int countLike(Long boardId, Long memberId);
    void insertLike(Long boardId, BasicUser basicUser);
    void deleteLike(Long boardId, Long memberId);

    int countLikeByBoard(Long boardId);

}