package com.zerogreen.zerogreeneco.service.detail;


import com.zerogreen.zerogreeneco.dto.detail.LikesDto;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;

import java.util.List;

public interface LikesService {
    //좋다구요
    void addLikes(Long sno, BasicUser basicUser);
    //안좋다구요
    void removeLike(Long sno, Long mno);
    //나 얼만큼 좋아해요
    Long cntLikes(Long sno);
    //회원의 가게별 라이크 카운팅
    Long cntMemberLike(Long sno, Long mno);
    //멤버별 전체 라이크 수 카운팅 (memberMyInfo)
    Long countLikesByUser(Long id);
    //회원별 찜한 가게 리스트 (memberMyInfo)
    List<LikesDto> getLikesByUser(Long id);

}
