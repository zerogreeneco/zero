package com.zerogreen.zerogreeneco.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zerogreen.eco.entity.userentity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m.id from BasicUser m where m.id =:memberId ")
    Long findmemberId(Long memberId);

    @Query("select m.nickname from Member m where m.id =:memberId ")
    String findmemberNick(Long memberId);

    Optional<Member> findByUsername(String username);

    Integer countByNickname(@Param("nickname") String nickname);
}
