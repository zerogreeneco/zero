package com.zerogreen.zerogreeneco.security.auth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
* @AuthenticationPrincipal
* 비회원인 경우 null로 반환
* 회원인 경우는 어노테이션이 사용된 객체(DTO 또는 엔티티)를 사용할 수 있다.
* */
@Retention(RetentionPolicy.RUNTIME) // 라이프 사이클 -> 어노테이션이 언제까지 살아남을지를 정함
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : basicUser" )
public @interface PrincipalUser {
}
