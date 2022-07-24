package com.zerogreen.zerogreeneco.controller;

import com.zerogreen.zerogreeneco.dto.detail.DetailReviewDto;
import com.zerogreen.zerogreeneco.dto.detail.LikesDto;
import com.zerogreen.zerogreeneco.dto.member.MemberUpdateDto;
import com.zerogreen.zerogreeneco.dto.member.PasswordUpdateDto;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;
import com.zerogreen.zerogreeneco.entity.userentity.VegetarianGrade;
import com.zerogreen.zerogreeneco.security.auth.PrincipalDetails;
import com.zerogreen.zerogreeneco.service.detail.DetailReviewService;
import com.zerogreen.zerogreeneco.service.detail.LikesService;
import com.zerogreen.zerogreeneco.service.file.FileService;
import com.zerogreen.zerogreeneco.service.user.BasicUserService;
import com.zerogreen.zerogreeneco.service.user.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final BasicUserService basicUserService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final LikesService likesService;
    private final DetailReviewService detailReviewService;
    private final FileService fileService;

    @ModelAttribute("member")
    public MemberUpdateDto memberUpdateDto() {
        return new MemberUpdateDto();
    }

    @ModelAttribute("password")
    private PasswordUpdateDto passwordUpdateDto() {
        return new PasswordUpdateDto();
    }

    @GetMapping("/account")
    public String memberInfoForm(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                 MemberUpdateDto memberUpdateDto, PasswordUpdateDto passwordUpdateDto, Model model) {

        MemberUpdateDto updateDto = memberService.toMemberUpdateDto(principalDetails.getUsername(), memberUpdateDto);
        log.info("UPDATEDTO={}", updateDto);

        model.addAttribute("member", updateDto);
        model.addAttribute("password", passwordUpdateDto);

        return "member/updateMember";
    }

    /*
     * 회원 정보 수정
     * */
    @PatchMapping("/account")
    @ResponseBody
    public ResponseEntity<Map<String, String>> memberInfoUpdate(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                @RequestBody Map<String, Object> params, HttpSession session) {
        Map<String, String> resultMap = new HashMap<>();

        String vegetarianGrade = params.get("vegetarianGrade").toString();
        VegetarianGrade parseVegan = VegetarianGrade.valueOf(vegetarianGrade);

        if (principalDetails.getBasicUser().getUserRole().equals(UserRole.USER)) {
            String nickname = params.get("nickname").toString();
            String phoneNumber = params.get("phoneNumber").toString();

            if ("".equals(nickname) || "".equals(phoneNumber)) {
                resultMap.put("result", "fail");
                return new ResponseEntity<>(resultMap, HttpStatus.OK);
            }

            session.removeAttribute("veganGrade");
            session.removeAttribute("loginUserNickname");
            memberService.memberUpdate(principalDetails.getId(), nickname, phoneNumber, parseVegan);
            session.setAttribute("veganGrade", vegetarianGrade);
            session.setAttribute("loginUserNickname", nickname);
        } else if (principalDetails.getBasicUser().getUserRole().equals(UserRole.AUTH_USER)) {
            session.removeAttribute("veganGrade");
            memberService.oauthMemberUpdate(principalDetails.getId(), parseVegan);
            session.setAttribute("veganGrade", vegetarianGrade);
        }

        resultMap.put("result", "success");
        log.info("회원 정보 수정 성공!!!!!");

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    /*
     * 비밀번호 변경
     * */
    @PatchMapping("/account/pwdChange")
    @ResponseBody
    public ResponseEntity<Map<String, String>> passwordChange(@Validated @ModelAttribute("password") PasswordUpdateDto passwordDto, BindingResult bindingResult,
                                                              @AuthenticationPrincipal PrincipalDetails principalDetails, HttpSession session) {
        Map<String, String> resultMap = new HashMap<>();

        if (passwordEncoder.matches(passwordDto.getPassword(), principalDetails.getPassword())) {
            basicUserService.passwordChange(principalDetails.getId(), passwordDto);
            resultMap.put("result", "success");
            session.invalidate(); // 비밀번호 변경 후 강제 로그아웃 후 다시 로그인
            log.info("비밀번호 변경 성공!!!!!");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        } else {
            resultMap.put("result", "fail");
            log.info("비밀번호 변경 실패....ㅠㅠ");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
    }

    // 회원 삭제
    @DeleteMapping("/account/deleteMember")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteMember(@Validated @ModelAttribute("password") PasswordUpdateDto passwordUpdateDto,
                                                            BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails, HttpSession session) {
        Map<String, String> resultMap = new HashMap<>();

        // dto로 넘어 온 데이터와 로그인 회원의 인코딩된 비밀번호 일치여부 확인
        boolean matches = passwordEncoder.matches(passwordUpdateDto.getPassword(), principalDetails.getPassword());

        if (bindingResult.hasErrors()) {
            if (!matches) {
                bindingResult.reject("discordPassword", null);
            }
        }

        if (matches) {
            basicUserService.memberDelete(principalDetails.getId());
            resultMap.put("result", "success");
            session.invalidate();
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        } else {
            resultMap.put("result", "fail");
        }
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("/memberMyInfo")
    public String myInfo(@AuthenticationPrincipal PrincipalDetails principalDetails,
                         Model model) {

        //회원별 남긴 리뷰 수 (memberMyInfo)
        model.addAttribute("reviewCount", detailReviewService.countReviewByUser(principalDetails.getId()));
        //회원별 좋아요 수 (memberMyInfo)
        model.addAttribute("likesCount", likesService.countLikesByUser(principalDetails.getId()));
        //회원별 리뷰 남긴 가게 리스트(memberMyInfo)
        List<DetailReviewDto> userReview = detailReviewService.getReviewByUser(principalDetails.getId());
        model.addAttribute("listOfReview", userReview);
        //회원별 찜한 가게 리스트 (memberMyInfo)
        List<LikesDto> likes = likesService.getLikesByUser(principalDetails.getId());
        model.addAttribute("listOfLikes", likes);

        return "member/memberMyInfo";
    }

    //카드리스트 스토어 이미지 불러오기
    @ResponseBody
    @GetMapping("/memberMyInfo/image/{storeName}{thumbnailName}")
    private Resource getStoreImages(@PathVariable("thumbnailName") String thumbnailName,
                                    @PathVariable("storeName") String storeName) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFullPathImage(thumbnailName, storeName));
    }

}
