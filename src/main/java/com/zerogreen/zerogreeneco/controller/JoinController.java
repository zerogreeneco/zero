package com.zerogreen.zerogreeneco.controller;

import com.zerogreen.zerogreeneco.dto.file.FileForm;
import com.zerogreen.zerogreeneco.dto.member.MemberJoinDto;
import com.zerogreen.zerogreeneco.dto.store.StoreJoinDto;
import com.zerogreen.zerogreeneco.entity.file.RegisterFile;
import com.zerogreen.zerogreeneco.entity.userentity.Member;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import com.zerogreen.zerogreeneco.entity.userentity.StoreType;
import com.zerogreen.zerogreeneco.entity.userentity.VegetarianGrade;
import com.zerogreen.zerogreeneco.service.file.FileService;
import com.zerogreen.zerogreeneco.service.mail.MailService;
import com.zerogreen.zerogreeneco.service.user.BasicUserService;
import com.zerogreen.zerogreeneco.service.user.MemberService;
import com.zerogreen.zerogreeneco.service.user.StoreMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class JoinController {

    @Value("${file.dir}")
    private String fileDir;

    @Value("C:/imageUpload/")
    private String imageFileDir;

    private final BasicUserService basicUserService;
    private final MemberService memberService;
    private final StoreMemberService storeMemberService;
    private final FileService fileService;
    private final MailService mailService;

    // 컨트롤러 전체에서 사용 가능 (vegan, storeType)
    @ModelAttribute("vegan")
    private VegetarianGrade[] vegetarianGrades() {
        VegetarianGrade[] vegetarianGrades = VegetarianGrade.values();
        return vegetarianGrades;
    }

    @ModelAttribute("storeTypes")
    private StoreType[] storeTypes() {
        StoreType[] storeTypes = StoreType.values();
        return storeTypes;
    }

    /*
     * 일반 회원 가입
     * */
    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") MemberJoinDto member) {

        return "register/registerForm";
    }

    @PostMapping("/add")
    public String addMember(@Validated @ModelAttribute("member") MemberJoinDto member, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        log.info("member={}", member);

        // 유효성 검사
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError allError : allErrors) {
                log.info("allError ={} ", allError);
            }
            return "register/registerForm";
        }

        // 회원가입 성공 로직
        Member joinMember = member.toMember(member);
        memberService.saveV2(joinMember);

        redirectAttributes.addAttribute("nickname", joinMember.getNickname());
        return "redirect:/members/welcome";
    }

    /*
     * 이메일 인증
     * */
    @PostMapping("/checkMail")
    @ResponseBody
    public HashMap<String, String> sendMail(String mail) {

        HashMap<String, String> keyMap = new HashMap<>();

        long count = basicUserService.countByUsername(mail);

        if (count == 1) {
            log.info("중복 회원 이메일 인증 불가");
            keyMap.put("fail", "아이디 중복");
            return keyMap;
        } else {
            log.info("이메일 인증 컨트롤러 OK");
            log.info("EMAIL ={}", mail);
            String key = mailService.createAuthKey();

            keyMap.put("key", key);
            log.info("Before Send Key={}", key);
            mailService.sendAuthMail(mail, key);
            log.info("key={}", key);
        }

        return keyMap;
    }

    /*
     * 환영 페이지
     * */
    @GetMapping("/welcome")
    public String welcome(@RequestParam("nickname") String nickname, Model model) {

        model.addAttribute("nickname", nickname);
        return "register/welcome";
    }

    /*
     * 가게 회원 가입
     * */
    @GetMapping("/store/add")
    public String storeAddForm(@ModelAttribute("store") StoreJoinDto storeJoinDto, @ModelAttribute("file") FileForm fileForm) {
        return "register/storeRegisterForm";
    }

    @PostMapping("/store/add")
    public String storeAdd(@RequestBody @Validated @ModelAttribute("store") StoreJoinDto storeJoinDto, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) throws IOException {

        // 기본 유효성 검사 (Null 체크 등)
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();

            for (ObjectError allError : allErrors) {
                log.info("ERROR CODE={}", allError);
            }
            return "register/storeRegisterForm";
        }

        MultipartFile attachFile = storeJoinDto.getAttachFile();

        // 첨부 파일 유효성 검사
        if (attachFile.isEmpty()) {
            bindingResult.reject("attachFile", null);
            return "register/storeRegisterForm";
        } else if (!attachFile.getContentType().startsWith("image")
                && !attachFile.getContentType().equals("application/pdf")) {
            log.warn("이미지 또는 PDF 파일이 아닙니다.");
            bindingResult.reject("incorrectAttachFile", null);
            return "register/storeRegisterForm";
        }

        // 회원가입 성공 로직
        RegisterFile uploadFile = fileService.saveFile(attachFile);
        StoreMember storeMember = new StoreJoinDto().toStoreMember(storeJoinDto);

        storeMemberService.save(storeMember, uploadFile);
        redirectAttributes.addAttribute("nickname", storeMember.getStoreName());

        return "redirect:/members/welcome";

    }

    /*
     * 닉네임 중복 확인
     * */
    @PostMapping("/nickname")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> nicknameDuplicateCheck(String nickname) {
        HashMap<String, Integer> resultMap = new HashMap<>();

        Integer count = memberService.countByNickname(nickname);

        if (count == 1) {
            resultMap.put("result", count);
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    /*
     * 연락처 중복 확인
     * */
    @PostMapping("/phoneNumber")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> phoneNumberDuplicateCheck(String phoneNumber) {
        HashMap<String, Integer> resultMap = new HashMap<>();

        Integer count = basicUserService.countByPhoneNumber(phoneNumber);

        if (count == 1) {
            resultMap.put("result", count);
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @PostMapping("/storeRegNum")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> storeRegNumDuplicateCheck(String storeRegNum) {
        HashMap<String, Integer> resultMap = new HashMap<>();

        Integer count = storeMemberService.countByStoreRegNum(storeRegNum);

        if (count == 1) {
            resultMap.put("result", count);
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}
