package com.zerogreen.zerogreeneco.service.user;

import com.zerogreen.zerogreeneco.dto.member.FindMemberDto;
import com.zerogreen.zerogreeneco.dto.member.PasswordUpdateDto;
import com.zerogreen.zerogreeneco.dto.store.NonApprovalStoreDto;
import com.zerogreen.zerogreeneco.dto.store.StoreUpdateDto;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;
import com.zerogreen.zerogreeneco.repository.user.BasicUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicUserServiceImpl implements BasicUserService{

    private final BasicUserRepository basicUserRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    * TEST ADMIN SAVE
    * */
    @Override
    public Long adminSave() {

        String encPassword = passwordEncoder.encode("1");
        return basicUserRepository.save(new BasicUser("ADMIN", null, encPassword, UserRole.ADMIN, true))
                .getId();
    }

    /*
    * 인증 받지 못한 가게 회원
    * */
    @Override
    public Page<NonApprovalStoreDto> findByNonApprovalStore(Pageable pageable) {
        return basicUserRepository.findByUnApprovalStore(pageable);
    }

    @Override
    public FindMemberDto findByUsernameAndPhoneNumber(String username, String phoneNumber) {
        BasicUser basicUser = basicUserRepository.findByUsernameAndPhoneNumber(username, phoneNumber).orElseThrow();
        return new FindMemberDto(basicUser.getUsername(), basicUser.getPhoneNumber());
    }

    @Override
    public FindMemberDto findByPhoneNumber(String phoneNumber) {
        BasicUser basicUser = basicUserRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        log.info("BasicUser={}", basicUser.getUsername());
        return new FindMemberDto(basicUser.getUsername());
    }

    /*
    * 카운트 쿼리
    * */
    @Override
    public int countByPhoneNumber(String phoneNumber) {
        log.info("SERVICE COUNT={}",phoneNumber);
        return basicUserRepository.countByPhoneNumber(phoneNumber);
    }

    @Override
    public long countByUsername(String username) {
        return basicUserRepository.countByUsername(username);
    }

    @Override
    public long countByUsernameAndPhoneNumber(String username, String phoneNumber) {
        return basicUserRepository.countByUsernameAndPhoneNumber(username, phoneNumber);
    }

    /*
    * UNSTORE -> STORE 권한 변경
    * */
    @Override
    @Transactional
    public void changeStoreUserRole(List<Long> memberId) {
        basicUserRepository.changeUserRole(memberId);
    }

    /*
    * 비승인 가게 회원 검색 조건 (가게 이름 / 가게 연락처 / 사업자 등록번호)
    * */
    @Override
    public Page<NonApprovalStoreDto> nonApprovalStoreSearch(NonApprovalStoreDto SearchCond, Pageable pageable) {
        return basicUserRepository.searchAndPaging(SearchCond, pageable);
    }

    /*
     * 가게 회원 정보 변경
     */
    @Override
    public StoreUpdateDto getStoreMember(Long id, StoreUpdateDto storeUpdateDto) {
        BasicUser basicUser = basicUserRepository.findById(id).orElseThrow();
        return new StoreUpdateDto(basicUser.getUsername(), basicUser.getPhoneNumber());
    }

    @Transactional
    @Override
    public void updateStoreMember(Long id, StoreUpdateDto storeUpdateDto) {
        BasicUser updateStoreMember = basicUserRepository.findById(id).orElseThrow();
        updateStoreMember.setPhoneNumber(storeUpdateDto.getPhoneNumber());
    }

    /*
    * 비밀번호 변경
    * */
    @Override
    @Transactional
    public void passwordChange(Long id, PasswordUpdateDto passwordDto) {
        BasicUser updateMember = basicUserRepository.findById(id).orElseThrow();

        String encPassword = passwordEncoder.encode(passwordDto.getNewPassword());

        updateMember.setPassword(encPassword);
    }

    @Override
    public void memberDelete(Long id) {
        basicUserRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BasicUser findByName(String userName) {
        return basicUserRepository.findByChatUsername(userName);
    }
}
