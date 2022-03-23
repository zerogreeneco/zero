package com.zerogreen.zerogreeneco.dto.paging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class RequestPageDto {

    private int page;
    private int size;

    public RequestPageDto() {
        this.page = 1;
        this.size = 1000;
    }

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }
}
