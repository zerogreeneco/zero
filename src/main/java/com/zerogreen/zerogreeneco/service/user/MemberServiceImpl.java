package com.zerogreen.zerogreeneco.service.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerogreen.eco.dto.member.MemberUpdateDto;
import zerogreen.eco.entity.userentity.Member;
import zerogreen.eco.entity.userentity.UserRole;
import zerogreen.eco.entity.userentity.VegetarianGrade;
import zerogreen.eco.repository.user.BasicUserRepository;
import zerogreen.eco.repository.user.MemberRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final BasicUserRepository basicUserRepository;
    private final MemberRepository memberRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;


    /*
     * 테스트 데이터용
     * */
    @Transactional
    @Override
    public Long save(Member member) {
        String encPassword = passwordEncoder.encode(member.getPassword());

        return memberRepository.save(new Member(member.getUsername(), member.getNickname(),
                        member.getPhoneNumber(), encPassword, UserRole.USER, member.getVegetarianGrade()))
                .getId();
    }

    /*
     * 회원가입
     * */
    @Transactional
    @Override
    public Long saveV2(Member member) {
        String encPassword = passwordEncoder.encode(member.getPassword());

        return memberRepository.save(Member.builder()
                        .username(member.getUsername())
                        .password(encPassword)
                        .nickname(member.getNickname())
                        .phoneNumber(member.getPhoneNumber())
                        .userRole(UserRole.USER)
                        .vegetarianGrade(member.getVegetarianGrade())
                        .authState(false)
                        .build())
                .getId();
    }

    /*
    * 닉네임 중복 확인
    * */
    @Override
    public Integer countByNickname(String nickname) {
        return memberRepository.countByNickname(nickname);
    }

    /*
     * 회원 인증 상태 변경 : false -> true
     * */
    @Transactional
    @Override
    public void changeAuthState(Long id) {
        Member joinMember = memberRepository.findById(id).orElseThrow();

        joinMember.setAuthState(true);
    }

    @Override
    public MemberUpdateDto toMemberUpdateDto(String username, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findByUsername(username).orElseThrow();

        return new MemberUpdateDto(member.getUsername(), member.getNickname(), member.getPhoneNumber(), member.getVegetarianGrade());
    }

    /*
     * 회원 정보 수정
     * */
    @Transactional
    @Override
    public void memberUpdate(Long id, String nickName, String phoneNumber, VegetarianGrade vegetarianGrade) {
        Member updateMember = memberRepository.findById(id).orElseThrow();

        // 더티 체킹
        updateMember.setNickname(nickName);
        updateMember.setPhoneNumber(phoneNumber);
        updateMember.setVegetarianGrade(vegetarianGrade);
    }

    /*
     * Oauth 회원 정보 수정
     * */
    @Transactional
    @Override
    public void oauthMemberUpdate(Long id, VegetarianGrade vegetarianGrade) {
        Member updateMember = memberRepository.findById(id).orElseThrow();

        updateMember.setVegetarianGrade(vegetarianGrade);
    }

    @Override
    public MemberUpdateDto detailMemberInfo(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow();

        return new MemberUpdateDto(member.getUsername(), member.getNickname());
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Long findAuthMember(String username) {
        return basicUserRepository.findByAuthUsername(username);
    }

}
