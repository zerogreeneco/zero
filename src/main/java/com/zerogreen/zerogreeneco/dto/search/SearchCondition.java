package com.zerogreen.zerogreeneco.dto.search;

import lombok.Getter;

@Getter
public class SearchCondition {
    private String content;
    private SearchType searchType;
    private StoreSearchType storeSearchType;

    public SearchCondition(String content, SearchType searchType) {
        this.content = content;
        this.searchType = searchType;
    }
    public SearchCondition(StoreSearchType searchType, String content){
        this.content = content;
        this.storeSearchType = searchType;
    }
}
