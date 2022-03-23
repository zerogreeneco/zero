package com.zerogreen.zerogreeneco.repository.user;

import zerogreen.eco.dto.store.NonApprovalStoreDto;
import zerogreen.eco.dto.store.StoreDto;
import zerogreen.eco.entity.userentity.UserRole;

import java.util.List;

public interface StoreMemberRepositoryCustom {
    List<NonApprovalStoreDto> findByApprovalStore(UserRole userRole);

    //스토어 db (Detail)
    StoreDto getStoreById(Long sno);

}
