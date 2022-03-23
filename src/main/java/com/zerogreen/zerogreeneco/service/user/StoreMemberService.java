package com.zerogreen.zerogreeneco.service.user;

import com.zerogreen.zerogreeneco.dto.detail.DetailReviewDto;
import com.zerogreen.zerogreeneco.dto.search.SearchCondition;
import com.zerogreen.zerogreeneco.dto.store.NonApprovalStoreDto;
import com.zerogreen.zerogreeneco.dto.store.StoreDto;
import com.zerogreen.zerogreeneco.entity.file.RegisterFile;
import com.zerogreen.zerogreeneco.entity.file.StoreImageFile;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import com.zerogreen.zerogreeneco.entity.userentity.StoreType;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

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
