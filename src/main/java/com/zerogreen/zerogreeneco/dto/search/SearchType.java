package com.zerogreen.zerogreeneco.dto.search;

public enum SearchType {
    CONTENT("내용"), WRITER("작성자");

    private final String searchName;

    SearchType(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchName() {
        return searchName;
    }
}
