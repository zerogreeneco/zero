package com.zerogreen.zerogreeneco.entity.userentity;

public enum StoreType {
    VEGAN_FOOD("비건 식당 또는 제로 웨이스트 포장 식당","비건"),
    GENERAL_FOOD("일반 음식점 (친환경)","친환경"),
    ECO_SHOP("친환경 용품가게","에코샵");

    private final String storeType;
    private final String storeShort;

    StoreType(String storeType, String storeShort) {
        this.storeType = storeType;
        this.storeShort = storeShort;
    }

    public String getStoreType() {
        return storeType;
    }

    public String getStoreShort() {
        return storeShort;
    }
}
