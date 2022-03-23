package com.zerogreen.zerogreeneco.repository.list;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import zerogreen.eco.dto.search.SearchCondition;
import zerogreen.eco.dto.store.StoreDto;
import zerogreen.eco.entity.userentity.StoreType;

public interface StoreListRepository {
    Slice<StoreDto> getShopList(Pageable pageable);
    Slice<StoreDto> getShopSearchList(Pageable pageable, SearchCondition searchCondition);
    Slice<StoreDto> getFoodList(Pageable pageable);
    Slice<StoreDto> getFoodTypeList(Pageable pageable, StoreType storeType);
    Slice<StoreDto> getFoodSearchList(Pageable pageable, SearchCondition searchCondition);
}
