package com.zerogreen.zerogreeneco.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiReturnDto<T> {
    private int count;
    private T data;

    public ApiReturnDto(T data) {
        this.data = data;
    }

    public ApiReturnDto(int count, T data) {
        this.count = count;
        this.data = data;
    }
}
