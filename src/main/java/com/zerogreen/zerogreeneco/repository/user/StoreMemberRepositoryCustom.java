package com.zerogreen.zerogreeneco.repository.user;


import com.zerogreen.zerogreeneco.dto.store.NonApprovalStoreDto;
import com.zerogreen.zerogreeneco.dto.store.StoreDto;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;

import java.util.List;

public interface StoreMemberRepositoryCustom {
    List<NonApprovalStoreDto> findByApprovalStore(UserRole userRole);

    //스토어 db (Detail)
    StoreDto getStoreById(Long sno);

}
