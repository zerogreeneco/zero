package com.zerogreen.zerogreeneco.dto.search;

public enum StoreSearchType {
    store_name("가게 이름"), item("메뉴/상품");

    private final String storeSearchType;

    StoreSearchType(String storeSearchType){
        this.storeSearchType = storeSearchType;
    }

    public String storeSearchType(){
        return storeSearchType;
    }
}