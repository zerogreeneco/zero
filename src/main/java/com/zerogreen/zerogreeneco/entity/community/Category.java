package com.zerogreen.zerogreeneco.entity.community;

import lombok.Getter;

@Getter
public enum Category {
    QNA("Q&A"), VEGAN("비건"), NEWS("동네 소식"),
    PLOGGING("줍깅"), PLOVING("플로빙");

    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
