package com.zerogreen.zerogreeneco.controller;

import com.zerogreen.zerogreeneco.dto.member.FindMemberDto;
import com.zerogreen.zerogreeneco.service.mail.MailService;
import com.zerogreen.zerogreeneco.service.user.BasicUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final BasicUserService basicUserService;
    private final MailService mailService;

    @GetMapping("/login")
    public String loginForm(String error, Model model, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("redirectURI", referer);

        if (error != null) {
            model.addAttribute("error", "아이디 비번 체크");
        }

        return "login/loginForm";
    }

    @GetMapping("/findMember/id")
    public String findIdForm(@ModelAttribute("findMember") FindMemberDto findMemberForm) {

        return "member/findMemberId";
    }

    /*
     * 이메일 찾기
     * */
    @PostMapping("/findMember/id")
    public String findId(@Validated @ModelAttribute("findMember") FindMemberDto findMemberDto,
                         BindingResult bindingResult, Model model) {

        String phoneNumber = findMemberDto.getPhoneNumber();

        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError allError : allErrors) {
                log.info("FIND ID={}", allError);
            }
        }
        long count = basicUserService.countByPhoneNumber(phoneNumber);
        model.addAttribute("count", count);

        if (count == 1) {
            FindMemberDto findUsername = basicUserService.findByPhoneNumber(phoneNumber);
            model.addAttribute("findResult", findUsername.getUsername());

            log.info("findMember={}", findUsername.getUsername());
            return "member/findMemberId :: #result-id";
        } else if(count == 0){
            bindingResult.reject("notExistMember", null, null);
        }
        return "member/findMemberId :: #result-id";
    }

    @GetMapping("/findMember/password")
    public String findPasswordForm(@ModelAttribute("findMember") FindMemberDto findMemberForm) {

        return "member/findMember";
    }

    /*
     * 비밀번호 찾기
     * */
    @PostMapping("/findMember/password")
    public String findPassword(@Validated @ModelAttribute("findMember") FindMemberDto findMemberDto,
                               BindingResult bindingResult) {
        String username = findMemberDto.getUsername();
        String phoneNum = findMemberDto.getPhoneNumber();

        log.info("USERNAME={}", username);
        log.info("PHONENUM={}", phoneNum);

        long count = basicUserService.countByUsernameAndPhoneNumber(username, phoneNum);

        if (count == 1) {
            mailService.sendTempPassword(username, phoneNum);
        } else if (count == 0){
            bindingResult.reject("notExistMember", null, null);
            return "member/findMember";
        }
            return "redirect:/login";
    }

}
