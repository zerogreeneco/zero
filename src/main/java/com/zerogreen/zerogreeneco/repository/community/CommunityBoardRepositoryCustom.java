package com.zerogreen.zerogreeneco.repository.community;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import zerogreen.eco.dto.community.CommunityRequestDto;
import zerogreen.eco.dto.community.CommunityResponseDto;
import zerogreen.eco.dto.search.SearchCondition;
import zerogreen.eco.entity.community.Category;

public interface CommunityBoardRepositoryCustom {

    CommunityResponseDto findDetailBoard(Long boardId);

    Slice<CommunityResponseDto> findAllCommunityList(Pageable pageable, SearchCondition condition);
    Slice<CommunityResponseDto> findAllCommunityList(Pageable pageable);
    Slice<CommunityResponseDto> findByCategory(Pageable pageable, Category category);

    void addViewCount(Long boardId);
    CommunityResponseDto findDetailView(Long id);

    CommunityRequestDto boardModify(Long boardId);

}
