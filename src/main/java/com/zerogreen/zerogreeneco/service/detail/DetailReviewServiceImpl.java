package com.zerogreen.zerogreeneco.service.detail;

import com.zerogreen.zerogreeneco.dto.detail.DetailReviewDto;
import com.zerogreen.zerogreeneco.entity.detail.DetailReview;
import com.zerogreen.zerogreeneco.entity.detail.ReviewImage;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import com.zerogreen.zerogreeneco.repository.detail.DetailReviewRepository;
import com.zerogreen.zerogreeneco.repository.detail.ReviewImageRepository;
import com.zerogreen.zerogreeneco.repository.user.StoreMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DetailReviewServiceImpl implements DetailReviewService {
    private final StoreMemberRepository storeMemberRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final DetailReviewRepository detailReviewRepository;


    //멤버리뷰 DB저장 (이미지 포함)
    @Override
    @Transactional
    public Long saveImageReview(String reviewText, Long sno, BasicUser basicUser, List<ReviewImage> reviewImages) {
        StoreMember storeMember = storeMemberRepository.findById(sno).orElseThrow();
        DetailReview saveReview = detailReviewRepository.save(new DetailReview(reviewText, basicUser, storeMember));

        detailReviewRepository.flush();

        if(reviewImages.size() != 0) {
            for (ReviewImage image : reviewImages) {
                log.info("aaaaaaaareviewImage " + image);

                reviewImageRepository.save(new ReviewImage(
                        image.getUploadFileName(), image.getReviewFileName(), image.getFilePath(), saveReview, storeMember,
                        "thumb_" + image.getReviewFileName()));
            }
        }
        return saveReview.getId();
    }

    //리스팅 (detail)
    @Override
    public List<DetailReviewDto> findByStore(Long sno) {
        List<DetailReview> reviewList = detailReviewRepository.findByStore(sno);
        return reviewList.stream().map(DetailReviewDto::new).collect(Collectors.toList());
    }

//    @Override
//    public Page<DetailReviewDto> findByStore1(Long sno, Pageable pageable) {
//        return detailReviewRepository.findByStore1(sno, pageable);
//    }

    //대댓글 (detail)
    @Override
    @Transactional
    public void saveNestedReview(String reviewText, Long sno, BasicUser basicUser, Long rno) {
        StoreMember storeMember = storeMemberRepository.findById(sno).orElseThrow();
        // 컨트롤러에서 넘어 온 부모 댓글의 PK로 해당 부모 댓글을 검색
        DetailReview parentReview = detailReviewRepository.findById(rno).orElseThrow();
        // 새로운 자식 댓글을 생성하고 저장
        DetailReview childReview = new DetailReview(reviewText, basicUser, storeMember);
        detailReviewRepository.save(childReview);
        // 부모 댓글에 자식 댓글을 리스트로 저장 (양방향 매핑)
        parentReview.addNestedReview(childReview);
    }

    //리뷰 수정 (detail)
    @Override
    public void modifyReview(DetailReviewDto detailReviewDto) {
        Optional<DetailReview> result = detailReviewRepository.findById(detailReviewDto.getRno());
        if(result.isPresent()) {
            DetailReview review = result.get();
            review.editReview(detailReviewDto.getReviewText());
            detailReviewRepository.save(review);
        }
    }

    //리뷰 삭제 (detail)
    @Override
    public void remove(Long rno) {
        detailReviewRepository.deleteById(rno);
    }

    //회원별 전체 리뷰 수 카운팅 (memberMyInfo)
    @Override
    public Long countReviewByUser(Long id) {
        return detailReviewRepository.countReviewByUser(id);
    }

    //회원별 리뷰남긴 가게 리스트 (memberMyInfo)
    @Override
    public List<DetailReviewDto> getReviewByUser(Long id) {
        return detailReviewRepository.getReviewByUser(id);
    }

    //Test
    @Override
    @Transactional
    public Long saveReviewTest(DetailReview detailReview) {
        StoreMember storeMember = storeMemberRepository.findById(detailReview.getStoreMember().getId()).orElseThrow();
        return detailReviewRepository.save(new DetailReview(detailReview.getReviewText(), detailReview.getReviewer(), storeMember))
                .getId(); // 생성자 없음 왜요,,?
    }

    //save comments Test
    @Override
    @Transactional
    public Long saveNestedReviewTest(DetailReview detailReview, Long rno) {
        StoreMember storeMember = storeMemberRepository.findById(detailReview.getStoreMember().getId()).orElseThrow();
        DetailReview parentReview = detailReviewRepository.findById(rno).orElseThrow();
        DetailReview childReview = new DetailReview(detailReview.getReviewText(), detailReview.getReviewer(), storeMember);
        detailReviewRepository.save(childReview);
        parentReview.addNestedReview(childReview);
        return childReview.getId();
    }

    //save reviews
/*
    @Override
    @Transactional
    public Long saveReview(String reviewText, Long sno, BasicUser basicUser) {
        StoreMember storeMember = storeMemberRepository.findById(sno).orElseThrow();
        DetailReview saveReview = detailReviewRepository.save(new DetailReview(reviewText, basicUser, storeMember));// 생성자 없음 왜요,,?
*/
/*
        if(reviewImages.size() != 0) {
            for (ReviewImage image : reviewImages) {
                log.info("aaaaaaaareviewImage " + image);

                reviewImageRepository.save(new ReviewImage(
                        image.getUploadFileName(), image.getReviewFileName(), image.getFilePath(), saveReview, storeMember));
            }
        }
*//*

        return saveReview.getId();
    }
*/


}
