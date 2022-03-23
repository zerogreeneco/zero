package com.zerogreen.zerogreeneco.repository.detail;

import zerogreen.eco.dto.detail.LikesDto;

import java.util.List;

public interface LikesRepositoryCustom {
    //회원별 찜한 가게 리스트 (memberMyInfo)
    List<LikesDto> getLikesByUser(Long id);

}
