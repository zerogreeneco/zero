package com.zerogreen.zerogreeneco.service.user;


import com.zerogreen.zerogreeneco.dto.member.MemberUpdateDto;
import com.zerogreen.zerogreeneco.entity.userentity.Member;
import com.zerogreen.zerogreeneco.entity.userentity.VegetarianGrade;

import java.util.Optional;

public interface MemberService {

    Long save(Member member);
    Long saveV2(Member member);
    void changeAuthState(Long id);
    void memberUpdate(Long id, String nickName, String phoneNumber, VegetarianGrade vegetarianGrade);
    MemberUpdateDto detailMemberInfo(String username);
    MemberUpdateDto toMemberUpdateDto(String username, MemberUpdateDto memberUpdateDto);
    void oauthMemberUpdate(Long id, VegetarianGrade vegetarianGrade);
    Optional<Member> findById(Long id);
    Long findAuthMember(String username);

    // 닉네임 중복체크
    Integer countByNickname(String nickname);

}
