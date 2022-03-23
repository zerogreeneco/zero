package com.zerogreen.zerogreeneco.entity.userentity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    USER("ROLE_USER"), AUTH_USER("ROLE_USER"),UN_STORE("ROLE_UN_STORE"), STORE("ROLE_STORE"), ADMIN("ROLE_ADMIN");

    private final String key;

}
