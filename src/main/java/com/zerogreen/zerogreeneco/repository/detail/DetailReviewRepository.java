package com.zerogreen.zerogreeneco.repository.detail;

import com.zerogreen.zerogreeneco.entity.detail.DetailReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailReviewRepository extends JpaRepository<DetailReview, Long>, DetailReviewRepositoryCustom {

    //Detail 리스팅
    @Query("select dr from DetailReview dr " +
            "join fetch dr.storeMember s " +
            "join fetch dr.reviewer r " +
            "where dr.depth = 1 and dr.storeMember.id =:sno")
    List<DetailReview> findByStore(@Param("sno") Long sno);

//    @Query("select dr from DetailReview dr " +
//            "join fetch dr.storeMember s " +
//            "join fetch dr.reviewer r " +
//            "where dr.depth = 1 and dr.storeMember.id =:sno")
//    Page<DetailReview> findByStore1(@Param("sno") Long sno, Pageable pageable);

    //memberMyInfo에 나타나는 회원별 리뷰 수
    @Query("select count(dr.id) from DetailReview dr " +
            "where dr.reviewer.id =:id ")
    Long countReviewByUser(@Param("id") Long id);

    //detail에 나타나는 가게별 리뷰 수
    @Query("select count(dr.id) from DetailReview dr " +
            "left outer join BasicUser bu on dr.reviewer.id = bu.id " +
            "where bu.userRole ='USER' and dr.storeMember.id =:sno")
    Long counting(@Param("sno") Long sno);

}
