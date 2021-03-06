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

    // ???????????? ???????????? ?????? ?????? (vegan, storeType)
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
     * ?????? ?????? ??????
     * */
    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") MemberJoinDto member) {

        return "register/registerForm";
    }

    @PostMapping("/add")
    public String addMember(@Validated @ModelAttribute("member") MemberJoinDto member, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        log.info("member={}", member);

        // ????????? ??????
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError allError : allErrors) {
                log.info("allError ={} ", allError);
            }
            return "register/registerForm";
        }

        // ???????????? ?????? ??????
        Member joinMember = member.toMember(member);
        memberService.saveV2(joinMember);

        redirectAttributes.addAttribute("nickname", joinMember.getNickname());
        return "redirect:/members/welcome";
    }

    /*
     * ????????? ??????
     * */
    @PostMapping("/checkMail")
    @ResponseBody
    public Map<String, String> sendMail(String mail) {

        Map<String, String> keyMap = new HashMap<>();

        long count = basicUserService.countByUsername(mail);

        if (count == 1) {
            log.info("?????? ?????? ????????? ?????? ??????");
            keyMap.put("fail", "????????? ??????");
            return keyMap;
        } else {
            log.info("????????? ?????? ???????????? OK");
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
     * ?????? ?????????
     * */
    @GetMapping("/welcome")
    public String welcome(@RequestParam("nickname") String nickname, Model model) {

        model.addAttribute("nickname", nickname);
        return "register/welcome";
    }

    /*
     * ?????? ?????? ??????
     * */
    @GetMapping("/store/add")
    public String storeAddForm(@ModelAttribute("store") StoreJoinDto storeJoinDto, @ModelAttribute("file") FileForm fileForm) {
        return "register/storeRegisterForm";
    }

    @PostMapping("/store/add")
    public String storeAdd(@RequestBody @Validated @ModelAttribute("store") StoreJoinDto storeJoinDto, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) throws IOException {

        // ?????? ????????? ?????? (Null ?????? ???)
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();

            for (ObjectError allError : allErrors) {
                log.info("ERROR CODE={}", allError);
            }
            return "register/storeRegisterForm";
        }

        MultipartFile attachFile = storeJoinDto.getAttachFile();

        // ?????? ?????? ????????? ??????
        if (attachFile.isEmpty()) {
            bindingResult.reject("attachFile", null);
            return "register/storeRegisterForm";
        } else if (!attachFile.getContentType().startsWith("image")
                && !attachFile.getContentType().equals("application/pdf")) {
            log.warn("????????? ?????? PDF ????????? ????????????.");
            bindingResult.reject("incorrectAttachFile", null);
            return "register/storeRegisterForm";
        }

        // ???????????? ?????? ??????
        RegisterFile uploadFile = fileService.saveFile(attachFile);
        StoreMember storeMember = new StoreJoinDto().toStoreMember(storeJoinDto);

        storeMemberService.save(storeMember, uploadFile);
        redirectAttributes.addAttribute("nickname", storeMember.getStoreName());

        return "redirect:/members/welcome";

    }

    /*
     * ????????? ?????? ??????
     * */
    @PostMapping("/nickname")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> nicknameDuplicateCheck(String nickname) {
        Map<String, Integer> resultMap = new HashMap<>();

        Integer count = memberService.countByNickname(nickname);

        if (count == 1) {
            resultMap.put("result", count);
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    /*
     * ????????? ?????? ??????
     * */
    @PostMapping("/phoneNumber")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> phoneNumberDuplicateCheck(String phoneNumber) {
        Map<String, Integer> resultMap = new HashMap<>();

        Integer count = basicUserService.countByPhoneNumber(phoneNumber);

        if (count == 1) {
            resultMap.put("result", count);
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @PostMapping("/storeRegNum")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> storeRegNumDuplicateCheck(String storeRegNum) {
        Map<String, Integer> resultMap = new HashMap<>();

        Integer count = storeMemberService.countByStoreRegNum(storeRegNum);

        if (count == 1) {
            resultMap.put("result", count);
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}
