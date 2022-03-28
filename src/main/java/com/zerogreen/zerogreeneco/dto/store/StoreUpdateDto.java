package com.zerogreen.zerogreeneco.dto.store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class StoreUpdateDto {
    private String userName;

    @NotBlank(message = "전화번호를 입력해주세요")
    private String phoneNumber;

    public StoreUpdateDto(String userName, String phoneNumber){
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }
}