package com.zerogreen.zerogreeneco.repository.detail;


import com.zerogreen.zerogreeneco.dto.detail.DetailReviewDto;

import java.util.List;

public interface DetailReviewRepositoryCustom {

    //회원별 리뷰남긴 가게 리스트 (memberMyInfo)
    List<DetailReviewDto> getReviewByUser(Long id);

    //가게 리뷰 리스트
    List<DetailReviewDto> getReviewByStore(Long id);
}
