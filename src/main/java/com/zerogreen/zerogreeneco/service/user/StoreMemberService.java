package com.zerogreen.zerogreeneco.service.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import zerogreen.eco.dto.detail.DetailReviewDto;
import zerogreen.eco.dto.search.SearchCondition;
import zerogreen.eco.dto.store.NonApprovalStoreDto;
import zerogreen.eco.dto.store.StoreDto;
import zerogreen.eco.entity.file.RegisterFile;
import zerogreen.eco.entity.file.StoreImageFile;
import zerogreen.eco.entity.userentity.StoreMember;
import zerogreen.eco.entity.userentity.StoreType;
import zerogreen.eco.entity.userentity.UserRole;

import java.util.List;

public interface StoreMemberService {
    //스토어 회원가입
    Long save(StoreMember storeMember, RegisterFile registerFile);

    //이친구뭐예용
    List<NonApprovalStoreDto> findByApprovalStore(UserRole userRole);

    //스토어 db (Detail)
    StoreDto getStore(Long sno);

    //List 출력
    Slice<StoreDto> getShopList(Pageable pageable);
    Slice<StoreDto> getShopSearchList(Pageable pageable, SearchCondition searchCondition);
    Slice<StoreDto> getFoodList(Pageable pageable);
    Slice<StoreDto> getFoodTypeList(Pageable pageable, StoreType storeType);
    Slice<StoreDto> getFoodSearchList(Pageable pageable, SearchCondition searchCondition);

    //storeMyInfo
    List<DetailReviewDto> getReviewByStore(Long id);

    //StoreUpdate
    StoreDto getStoreInfo(Long id, StoreDto storeDto);
    void updateStore(Long id, StoreDto storeDto, List<StoreImageFile> storeImageFile);

    int countByStoreRegNum(String storeRegNum);

    //스토어 회원가입 테스트 데이터용
    Long saveV2(StoreMember storeMember, RegisterFile register);
    Long saveV3(StoreMember storeMember);

}
