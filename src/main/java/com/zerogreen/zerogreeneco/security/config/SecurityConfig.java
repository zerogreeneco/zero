package com.zerogreen.zerogreeneco.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import com.zerogreen.zerogreeneco.security.auth.PrincipalDetailsService;
import com.zerogreen.zerogreeneco.security.auth.oauth.service.CustomOAuth2UserService;
import com.zerogreen.zerogreeneco.security.handler.CustomAuthenticationFailureHandler;
import com.zerogreen.zerogreeneco.security.handler.LoginSuccessHandler;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalDetailsService principalDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler("/");
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler CustomFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /*
    * 시큐리티가 대신 로그인 로직을 진행할 때 password를 가로채는데
    * 해당 password를 뭘로 해쉬해서 회원가입 되었은지 알아야
    * 동잏한 해쉬로 암호화해서 DB에 해쉬랑 비교할 수 있음
   * */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/users/**").authenticated()
                .antMatchers("/storeUsers/**").access("hasRole('ROLE_STORE') or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/profile").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 대신 로그인을 진행
//                    .defaultSuccessUrl("/")
                .successHandler(loginSuccessHandler())
                .failureHandler(CustomFailureHandler())
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint() // OAuth2 로그인 성공 후 사용자 정보를 가져올 때 설정 담당
                .userService(customOAuth2UserService);
                    // OAuth2 로그인 후 사용자 정보를 가져온 상태에서 추가로 진행할 기능 명시 가능
                    // UserService 인터페이스의 구현체를 등록

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**")
                .antMatchers("/css/**", "/js/**","/img/**","/members/bootstrap/**");

    }

}
