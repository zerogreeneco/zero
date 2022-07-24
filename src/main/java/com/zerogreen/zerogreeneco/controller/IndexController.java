package com.zerogreen.zerogreeneco.controller;

import com.zerogreen.zerogreeneco.dto.store.NonApprovalStoreDto;
import com.zerogreen.zerogreeneco.entity.userentity.Member;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;
import com.zerogreen.zerogreeneco.repository.user.MemberRepository;
import com.zerogreen.zerogreeneco.security.auth.PrincipalDetails;
import com.zerogreen.zerogreeneco.service.community.CommunityBoardService;
import com.zerogreen.zerogreeneco.service.user.StoreMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@RequiredArgsConstructor
public class IndexController {

    private final StoreMemberService storeMemberService;
    private final MemberRepository memberRepository;
    private final CommunityBoardService boardService;

    @GetMapping({"", "/"})
    public String approvedStore(Model model, UserRole userRole, HttpSession session,
                                @AuthenticationPrincipal PrincipalDetails principalDetails, Authentication authentication) {

        // 로그인시, session에 담길 데이터
        if (Objects.nonNull(principalDetails) ) {
            session.setAttribute("memberId", principalDetails.getBasicUser().getId());

            if (UserRole.USER.equals(principalDetails.getBasicUser().getUserRole())) {
                session.setAttribute("veganGrade", principalDetails.getVegetarianGrade());
            }

            if (principalDetails.getBasicUser() instanceof Member) {
                session.setAttribute("loginUserNickname", ((Member) principalDetails.getBasicUser()).getNickname());
            } else if (principalDetails.getBasicUser() instanceof StoreMember) {
                session.setAttribute("loginUserNickname", ((StoreMember) principalDetails.getBasicUser()).getStoreName());
            }
        }

        List<NonApprovalStoreDto> result = storeMemberService.findByApprovalStore(userRole);
        model.addAttribute("approval", result);
        log.info(result+"yjyjyjyjyjyjyjyj");
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody
    String user(Model model) {
        model.addAttribute("user", "USER");
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody
    String admin(Model model) {
        model.addAttribute("admin", "admin");
        return "admin";
    }

    @GetMapping("/store")
    public @ResponseBody
    String store(Model model) {
        model.addAttribute("store", "store");
        return "store";
    }

    @Secured("USER")
    @GetMapping("/info")
    public @ResponseBody
    String info() {
        return "개인정보";
    }

//    @GetMapping("/chatting/{memberId}")
//    public String detailView(@PathVariable("memberId") Long memberId, Model model) {
//
//        model.addAttribute("chat", boardService.findId(memberId));
//        log.info(boardService.findId(memberId)+"detailViewyjyjyjyjyjyjyjyjyj");
//        return "/chat/chatIndex";
//    }

    @GetMapping("/chatting/{boardId}")
    public String detailView(@PathVariable("boardId") Long boardId, Model model) {
        model.addAttribute("user", memberRepository.findmemberId(boardId));
        log.info(memberRepository.findmemberId(boardId)+"er1111111");
        model.addAttribute("nick", memberRepository.findmemberNick(boardId));
        log.info(memberRepository.findmemberNick(boardId)+"er2222222");
        return "chat/chatIndex";
    }

    @GetMapping("/chatting/chatIndex")
    public void chat2() {
    }
}
