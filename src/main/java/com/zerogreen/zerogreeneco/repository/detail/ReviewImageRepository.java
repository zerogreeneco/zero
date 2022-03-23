package com.zerogreen.zerogreeneco.repository.detail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zerogreen.eco.entity.detail.ReviewImage;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    //스토어별 리뷰 이미지 리스트
    @Query("select img from ReviewImage img where img.storeMember.id =:sno")
    List<ReviewImage> findByStore(@Param("sno") Long sno);

}
