package com.zerogreen.zerogreeneco.dto.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PasswordUpdateDto {

    @NotBlank
    private String password;

    @NotBlank
    private String newPassword;

    public PasswordUpdateDto() {
    }

    public PasswordUpdateDto(String password, String newPassword) {
        this.password = password;
        this.newPassword = newPassword;
    }
}
