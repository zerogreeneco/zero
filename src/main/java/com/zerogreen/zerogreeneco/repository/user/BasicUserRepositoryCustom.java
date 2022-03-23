package com.zerogreen.zerogreeneco.repository.user;

import com.zerogreen.zerogreeneco.dto.store.NonApprovalStoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface BasicUserRepositoryCustom {

    Long findByAuthUsername(String username);

    Page<NonApprovalStoreDto> findByUnApprovalStore(Pageable pageable);
    Page<NonApprovalStoreDto> findByUnApprovalStoreSearch(Pageable pageable, String searchType, String searchText);

    Page<NonApprovalStoreDto> searchAndPaging(NonApprovalStoreDto condition, Pageable pageable);

    @Modifying
    void changeUserRole(List<Long> memberId);
}
