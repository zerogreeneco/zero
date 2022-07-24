package com.zerogreen.zerogreeneco.controller;

import com.zerogreen.zerogreeneco.dto.detail.DetailReviewDto;
import com.zerogreen.zerogreeneco.dto.member.PasswordUpdateDto;
import com.zerogreen.zerogreeneco.dto.store.StoreDto;
import com.zerogreen.zerogreeneco.dto.store.StoreMenuDto;
import com.zerogreen.zerogreeneco.dto.store.StoreUpdateDto;
import com.zerogreen.zerogreeneco.entity.file.StoreImageFile;
import com.zerogreen.zerogreeneco.entity.userentity.StoreType;
import com.zerogreen.zerogreeneco.entity.userentity.VegetarianGrade;
import com.zerogreen.zerogreeneco.security.auth.PrincipalDetails;
import com.zerogreen.zerogreeneco.service.file.FileService;
import com.zerogreen.zerogreeneco.service.store.StoreImageService;
import com.zerogreen.zerogreeneco.service.store.StoreMenuService;
import com.zerogreen.zerogreeneco.service.user.BasicUserService;
import com.zerogreen.zerogreeneco.service.user.StoreMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final BasicUserService basicUserService;
    private final StoreMemberService storeMemberService;
    private final StoreMenuService storeMenuService;
    private final StoreImageService storeImageService;
    private final FileService fileService;

    @ModelAttribute("storeTypes")
    private StoreType[] storeTypes() {
        StoreType[] storeTypes = StoreType.values();
        return storeTypes;
    }

    @ModelAttribute("vegan")
    private VegetarianGrade[] vegetarianGrades() {
        VegetarianGrade[] vegetarianGrades = VegetarianGrade.values();
        return vegetarianGrades;
    }

    //내정보 메인
    @GetMapping("/myInfo")
    public String storeMyInfo(@AuthenticationPrincipal PrincipalDetails principalDetails,
                              Model model) {

        model.addAttribute("info", storeMemberService.getStore(principalDetails.getId()));

        List<DetailReviewDto> reviews = storeMemberService.getReviewByStore(principalDetails.getId());
        model.addAttribute("review", reviews);
        Collections.reverse(reviews);

        return "store/myInfo";
    }

    //가게 정보 수정 페이지
    @GetMapping("/update/info")
    public String updateStoreInfo(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                  StoreDto storeDto, Model model) {

        model.addAttribute("storeInfo",
                storeMemberService.getStoreInfo(principalDetails.getBasicUser().getId(), storeDto));
        model.addAttribute("tableList", storeMenuService.getStoreMenu(principalDetails.getId()));
        model.addAttribute("storeImageList", storeImageService.getImageByStore(principalDetails.getId()));

        return "store/updateInfo";
    }

    //가게 정보 수정
    @PostMapping("/update/info")
    public String updateStoreInfo(@Validated @ModelAttribute("storeInfo") StoreDto storeDto, BindingResult bindingResult,
                                  @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) throws IOException {

        List<StoreDto> storeImageList = storeImageService.getImageByStore(principalDetails.getId());
        List<StoreMenuDto> tableList = storeMenuService.getStoreMenu(principalDetails.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("storeImageList", storeImageList);
            model.addAttribute("tableList", tableList);

            return "store/updateInfo";
        }

        List<MultipartFile> attachedFiles = storeDto.getUploadFiles();

        for (MultipartFile uploadFile : attachedFiles) {
            if (!uploadFile.getContentType().startsWith("image")
                    && !uploadFile.getContentType().endsWith("octet-stream")) {
                model.addAttribute("storeImageList", storeImageList);
                model.addAttribute("tableList", tableList);

                bindingResult.reject("notImageFile", "사진을 첨부해주세요");

                return "store/updateInfo";
            }
        }

        //이미지 로컬 저장
        List<StoreImageFile> storeImageFiles = fileService.storeImageFiles(storeDto.getUploadFiles(), storeDto.getStoreName());
        //이미지 DB 저장, 회원정보 수정
        storeMemberService.updateStore(principalDetails.getId(), storeDto, storeImageFiles);

        return "redirect:/stores/myInfo";
    }

    //비건 등급 포함 테이블 등록
    @PostMapping("/update/gradeTable")
    public String updateGradeList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                  Model model, HttpServletRequest request) {
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        VegetarianGrade vegetarianGrade = VegetarianGrade.valueOf(request.getParameter("grade"));

        storeMenuService.updateStoreMenu(principalDetails.getId(), name, price, vegetarianGrade);

        model.addAttribute("tableList", storeMenuService.getStoreMenu(principalDetails.getId()));

        return "store/updateInfo :: #grade-table";
    }

    //테이블 등록
    @PostMapping("/update/table")
    public String updateMenuList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                 Model model, HttpServletRequest request) {

        String name = request.getParameter("name");
        String price = request.getParameter("price");

        storeMenuService.updateStoreMenu(principalDetails.getId(), name, price);

        model.addAttribute("tableList", storeMenuService.getStoreMenu(principalDetails.getId()));

        return "store/updateInfo :: #list-table";
    }

    //메뉴 삭제
    @DeleteMapping("update/grade/delete")
    public String gradeDelete(@AuthenticationPrincipal PrincipalDetails principalDetails,
                              Model model, HttpServletRequest request) {

        Long id = Long.valueOf(request.getParameter("id"));
        storeMenuService.deleteMenu(id);

        model.addAttribute("tableList", storeMenuService.getStoreMenu(principalDetails.getId()));

        return "store/updateInfo :: #grade-table";
    }

    //상품 삭제
    @DeleteMapping("update/table/delete")
    public String tableDelete(@AuthenticationPrincipal PrincipalDetails principalDetails,
                              Model model, HttpServletRequest request) {

        Long id = Long.valueOf(request.getParameter("id"));
        storeMenuService.deleteMenu(id);

        model.addAttribute("tableList", storeMenuService.getStoreMenu(principalDetails.getId()));

        return "store/updateInfo :: #list-table";
    }

    //스토어 이미지 불러오기
    @ResponseBody
    @GetMapping("/update/image/{storeName}{storeFileName}")
    private Resource getStoreImages(@PathVariable("storeFileName") String storeFileName,
                                    @PathVariable("storeName") String storeName) throws MalformedURLException {

        return new UrlResource("file:" + fileService.getFullPathImage(storeFileName, storeName));
    }

    //이미지 삭제
    @DeleteMapping("update/img/delete")
    @ResponseBody
    public ResponseEntity<Map<String, String>> imgDelete(HttpServletRequest request) {

        Map<String, String> resultMap = new HashMap<>();
        Long id = Long.valueOf(request.getParameter("id"));
        String filePath = request.getParameter("filePath");
        String thumb = request.getParameter("thumb");

        storeImageService.deleteImg(id, filePath, thumb);

        resultMap.put("key", "success");

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    //회원 정보 수정
    @GetMapping("/update/account")
    public String storeAccount(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model,
                               StoreUpdateDto storeUpdateDto, PasswordUpdateDto passwordUpdateDto) {
        model.addAttribute("member",
                basicUserService.getStoreMember(principalDetails.getBasicUser().getId(), storeUpdateDto));
        model.addAttribute("password", passwordUpdateDto);
        return "store/updateStoreMember";
    }

    @PostMapping("/update/account")
    public String updateAccount(@Validated @ModelAttribute("member") StoreUpdateDto storeUpdateDto, BindingResult bindingResult,
                                @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // 기본 유효성 검사 (Null 체크 등)
        if (bindingResult.hasErrors()) {

            return "store/updateStoreMember";
        }
        basicUserService.updateStoreMember(principalDetails.getId(), storeUpdateDto);

        return "redirect:/stores/myInfo";
    }
}
