package com.zerogreen.zerogreeneco.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import zerogreen.eco.dto.member.FindMemberDto;
import zerogreen.eco.dto.member.PasswordUpdateDto;
import zerogreen.eco.dto.store.NonApprovalStoreDto;
import zerogreen.eco.dto.store.StoreUpdateDto;
import zerogreen.eco.entity.userentity.BasicUser;

import java.util.List;

public interface BasicUserService {

    Long adminSave();
    BasicUser findByName(String username);
    Page<NonApprovalStoreDto> findByNonApprovalStore(Pageable pageable);

    FindMemberDto findByPhoneNumber(String phoneNumber);
    FindMemberDto findByUsernameAndPhoneNumber(String username, String phoneNumber);

    Page<NonApprovalStoreDto> nonApprovalStoreSearch(NonApprovalStoreDto SearchCond, Pageable pageable);

    StoreUpdateDto getStoreMember(Long id, StoreUpdateDto storeUpdateDto);
    void updateStoreMember(Long id, StoreUpdateDto storeUpdateDto);
    void passwordChange(Long id, PasswordUpdateDto passwordUpdateDto);

    int countByPhoneNumber(String phoneNumber);
    long countByUsername(String username);

    long countByUsernameAndPhoneNumber(String username, String phoneNumber);

    void changeStoreUserRole(List<Long> memberId);

    void memberDelete(Long id);
}
