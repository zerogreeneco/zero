package com.zerogreen.zerogreeneco.dto.search;

public enum StoreSearchType {
    STORE_NAME("가게 이름"), ITEM("메뉴/상품");

    private final String storeSearchType;

    StoreSearchType(String storeSearchType){
        this.storeSearchType = storeSearchType;
    }

    public String storeSearchType(){
        return storeSearchType;
    }
}