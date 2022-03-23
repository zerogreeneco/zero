package com.zerogreen.zerogreeneco.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final String DEFAULT_FAILURE_URL = "/login?error";

    public CustomAuthenticationFailureHandler() {}

    String errorMsg = "";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        log.info("LOGIN ERROR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        // 아이디 또는 비밀번호 불일치
        if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException) {
            errorMsg = "아이디 또는 비밀번호를 다시 확인해주세요.";
            log.info("아이디 비번 오류 >>>>>>>>>>>>>>>>>>");
        } else if (exception instanceof DisabledException) {
            errorMsg = "계정이 비활성화되었습니다.";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMsg = "비밀번호 유효기간이 만료되었습니다.";
        } else {
            errorMsg = "알 수 없는 이유로 로그인에 실패했습니다. 관리자에게 문의하세요.";
        }

        request.setAttribute("errorMsg", errorMsg);

        // redirect 주소
        request.getRequestDispatcher(DEFAULT_FAILURE_URL).forward(request, response);
    }
}
