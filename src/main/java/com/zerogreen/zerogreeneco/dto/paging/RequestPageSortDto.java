package com.zerogreen.zerogreeneco.dto.paging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class RequestPageSortDto {

    private int page;
    private int size;

    public RequestPageSortDto() {
        this.page = 1;
        this.size = 12;
    }

    public Pageable getPageableSort(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }
    public Pageable getPageableSort() {
        return PageRequest.of(page - 1, size);
    }
}
