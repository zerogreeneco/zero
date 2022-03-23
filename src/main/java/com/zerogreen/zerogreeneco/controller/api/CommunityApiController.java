package com.zerogreen.zerogreeneco.controller.api;

import com.zerogreen.zerogreeneco.dto.api.ApiReturnDto;
import com.zerogreen.zerogreeneco.dto.community.CommunityReplyDto;
import com.zerogreen.zerogreeneco.dto.community.CommunityResponseDto;
import com.zerogreen.zerogreeneco.dto.paging.RequestPageSortDto;
import com.zerogreen.zerogreeneco.service.community.CommunityBoardService;
import com.zerogreen.zerogreeneco.service.community.CommunityReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommunityApiController {
    private final CommunityBoardService boardService;
    private final CommunityReplyService replyService;

    @GetMapping(value = "/communityList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiReturnDto<Slice<CommunityResponseDto>> communityHomeFormV2(@RequestParam(value = "category", required = false) Category category,
                                                                         RequestPageSortDto requestPageDto, Model model) {

        Pageable pageable = requestPageDto.getPageableSort(Sort.by("title").descending());

        log.info("CATEGORY={}", category);

        if (category == null) {
            Slice<CommunityResponseDto> allCommunityBoard = boardService.findAllCommunityBoard(pageable);
            int size = allCommunityBoard.getSize();
            return new ApiReturnDto<>(size, allCommunityBoard);
        } else {
            Slice<CommunityResponseDto> sorByCategory = boardService.findByCategory(pageable, category);
            int size = sorByCategory.getSize();
            return new ApiReturnDto<>(size, sorByCategory);
        }
    }

    @GetMapping(value = "/communityReplyList", produces = MediaType.APPLICATION_JSON_VALUE)
    private ApiReturnDto<List<CommunityReplyDto>> communityReplyList(@RequestParam(value = "boardId") Long boardId) {
        List<CommunityReplyDto> replyList = replyService.findReplyByBoardId(boardId);
        int size = replyList.size();
        return new ApiReturnDto<>(size, replyList);
    }
}
