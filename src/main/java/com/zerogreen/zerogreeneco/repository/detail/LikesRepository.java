package com.zerogreen.zerogreeneco.repository.detail;

import com.zerogreen.zerogreeneco.entity.detail.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LikesRepository extends JpaRepository<Likes, Long>, LikesRepositoryCustom {

    //전체 좋아요 수 (Detail)
    @Query("select count(l.id) from Likes l where l.storeMember.id =:sno")
    Long counting(@Param("sno") Long sno);

    // 회원별 좋아요 유무 (Detail)
    @Query("select count(l.id) from Likes l where l.storeMember.id =:sno " +
            "and l.basicUser.id =:mno")
    Long cntMemberLike(@Param("sno") Long sno, @Param("mno") Long mno);

    // 좋아요 취소 (Detail)
    @Transactional
    @Modifying
    @Query("delete from Likes l where l.storeMember.id =:sno and l.basicUser.id =:mno ")
    void deleteMemberLikes(@Param("sno") Long sno, @Param("mno") Long mno);

    //회원별 전체 좋아요 수 (memberMyInfo)
    @Query("select count(l.id) from Likes l where l.basicUser.id =:id ")
    Long countLikesByUser(@Param("id") Long id);

}
