package com.zerogreen.zerogreeneco.security.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zerogreen.eco.entity.userentity.BasicUser;
import zerogreen.eco.repository.user.BasicUserRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login")으로 설정
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadByUsername이 실행
@Service
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private BasicUserRepository basicUserRepository;

    /*
    * 시큐리티 세션 (Authentication (UserDetails))
    * 스프링이 로그인 요청을 할 때 username과 password 변수 2개를 가로채는데
    * password 부분을 알아서 하기 때문에 username이 DB에 있는지 확인해주는 역할할
   * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        BasicUser principal = basicUserRepository.findByUsername(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다." + username);
                });

        return new PrincipalDetails(principal); // 시큐리티 세션에 유저 정보 저장
    }
}

