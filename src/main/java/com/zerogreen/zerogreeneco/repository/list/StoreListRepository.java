package com.zerogreen.zerogreeneco.repository.list;

import com.zerogreen.zerogreeneco.dto.search.SearchCondition;
import com.zerogreen.zerogreeneco.dto.store.StoreDto;
import com.zerogreen.zerogreeneco.entity.userentity.StoreType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StoreListRepository {
    Slice<StoreDto> getShopList(Pageable pageable);
    Slice<StoreDto> getShopSearchList(Pageable pageable, SearchCondition searchCondition);
    Slice<StoreDto> getFoodList(Pageable pageable);
    Slice<StoreDto> getFoodTypeList(Pageable pageable, StoreType storeType);
    Slice<StoreDto> getFoodSearchList(Pageable pageable, SearchCondition searchCondition);
}
