package com.zerogreen.zerogreeneco.repository.community;

import com.zerogreen.zerogreeneco.dto.community.CommunityRequestDto;
import com.zerogreen.zerogreeneco.dto.community.CommunityResponseDto;
import com.zerogreen.zerogreeneco.dto.search.SearchCondition;
import com.zerogreen.zerogreeneco.entity.community.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommunityBoardRepositoryCustom {

    CommunityResponseDto findDetailBoard(Long boardId);

    Slice<CommunityResponseDto> findAllCommunityList(Pageable pageable, SearchCondition condition);
    Slice<CommunityResponseDto> findAllCommunityList(Pageable pageable);
    Slice<CommunityResponseDto> findByCategory(Pageable pageable, Category category);

    void addViewCount(Long boardId);
    CommunityResponseDto findDetailView(Long id);

    CommunityRequestDto boardModify(Long boardId);

}
