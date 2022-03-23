package com.zerogreen.zerogreeneco.service.detail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerogreen.eco.dto.detail.LikesDto;
import zerogreen.eco.entity.detail.Likes;
import zerogreen.eco.entity.userentity.BasicUser;
import zerogreen.eco.entity.userentity.StoreMember;
import zerogreen.eco.repository.detail.LikesRepository;
import zerogreen.eco.repository.user.StoreMemberRepository;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likesRepository;
    private final StoreMemberRepository storeMemberRepository;

    //좋아요
    @Override
    @Transactional
    public void addLikes(Long sno, BasicUser basicUser) {
        StoreMember findStore = storeMemberRepository.findById(sno).orElseThrow();
        likesRepository.save(new Likes(basicUser, findStore));
    }

    //안좋아요 흥!
    @Override
    public void removeLike(Long sno, Long mno) {
        likesRepository.deleteMemberLikes(sno, mno);
    }

    //카운팅스타~ 밤하늘의 퍼어어얼
    @Override
    public Long cntLikes(Long sno) {
        return likesRepository.counting(sno);
    }

    //회원의 가게별 라이크 카운팅
    @Override
    public Long cntMemberLike(Long sno, Long mno) {
        return likesRepository.cntMemberLike(sno, mno);
    }

    //회원별 전체 라이크 수 카운팅 (memberMyInfo)
    @Override
    public Long countLikesByUser(Long id) {
        return likesRepository.countLikesByUser(id);
    }

    //회원별 찜한 가게 리스트 (memberMyInfo)
    @Override
    public List<LikesDto> getLikesByUser(Long id) {
        return likesRepository.getLikesByUser(id);
    }
}

