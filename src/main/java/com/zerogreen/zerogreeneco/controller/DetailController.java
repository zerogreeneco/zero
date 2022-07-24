package com.zerogreen.zerogreeneco.controller;

import com.zerogreen.zerogreeneco.dto.detail.DetailReviewDto;
import com.zerogreen.zerogreeneco.dto.detail.ReviewImageDto;
import com.zerogreen.zerogreeneco.dto.paging.RequestPageSortDto;
import com.zerogreen.zerogreeneco.dto.store.StoreDto;
import com.zerogreen.zerogreeneco.dto.store.StoreMenuDto;
import com.zerogreen.zerogreeneco.entity.detail.ReviewImage;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;
import com.zerogreen.zerogreeneco.security.auth.PrincipalDetails;
import com.zerogreen.zerogreeneco.security.auth.PrincipalUser;
import com.zerogreen.zerogreeneco.service.detail.DetailReviewService;
import com.zerogreen.zerogreeneco.service.detail.LikesService;
import com.zerogreen.zerogreeneco.service.detail.ReviewImageService;
import com.zerogreen.zerogreeneco.service.file.FileService;
import com.zerogreen.zerogreeneco.service.store.StoreImageService;
import com.zerogreen.zerogreeneco.service.store.StoreMenuService;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class DetailController {

    private final StoreMemberService storeMemberService;
    private final LikesService likesService;
    private final ReviewImageService reviewImageService;
    private final DetailReviewService detailReviewService;
    private final StoreImageService storeImageService;
    private final FileService fileService;
    private final StoreMenuService storeMenuService;


    //상세페이지
    @GetMapping("/page/detail/{sno}")
    public String detail(@PathVariable("sno") Long sno, Model model,
                         @Validated @ModelAttribute("review") DetailReviewDto reviewDto, BindingResult bindingResult,
                         @PrincipalUser BasicUser basicUser, RequestPageSortDto requestPageSortDto,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException{

        //스토어 데이터 + 회원/비회원
        StoreDto storeDto = storeMemberService.getStore(sno);
        log.info("?????Controller " + sno);
        log.info("<<<<< " + storeDto.getSno());
        log.info("<<<<< " + storeDto.getLikesCount());
        log.info("<<<<< " + storeDto.getReviewCount());

        if (Objects.nonNull(principalDetails) ) {
            model.addAttribute("getStore",storeDto);
        } else {
            model.addAttribute("getStore",storeDto);
            model.addAttribute("member", principalDetails.getUsername());
            //리뷰어쩌구..
            model.addAttribute("rvMember", principalDetails.getId());
            //가게별 개별 라이크
            model.addAttribute("cntLike", likesService.cntMemberLike(sno, principalDetails.getId()));
        }

        //스토어 이미지 리스트
        List<StoreDto> image = storeImageService.getImageByStore(sno);
        model.addAttribute("storeImageList", image);

        //메뉴 리스트
        List<StoreMenuDto> menuList = storeMenuService.getStoreMenu(sno);
        model.addAttribute("menuList", menuList);

        //리뷰 텍스트 리스트
        List<DetailReviewDto> result = detailReviewService.findByStore(sno);
        model.addAttribute("memberReview", result);
        Collections.reverse(result);

        //리뷰 이미지 리스트 (스토어전체)
        List<ReviewImageDto> reviewImages = reviewImageService.findByStore(sno);
        if (reviewImages.size() > 0) {
            model.addAttribute("reviewImageList", reviewImages);
            Collections.reverse(reviewImages);
        }

        return "page/detail";
    }


    // 이미지 포함 리뷰 db
    @PostMapping("/page/detail/addReview/{sno}")
    public String addReview(@PathVariable("sno") Long sno, Model model,
                            @Validated @ModelAttribute("review") DetailReviewDto reviewDto, BindingResult bindingResult,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        //유효성검사 임시
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("storeImageList", storeImageService.getImageByStore(sno));
//            model.addAttribute("getStore",storeMemberService.getStore(sno));
//            model.addAttribute("menuList", storeMenuService.getStoreMenu(sno));
//            model.addAttribute("memberReview", detailReviewService.findByStore(sno));
//            model.addAttribute("cntLike", likesService.cntMemberLike(sno, principalDetails.getId()));
//            List<ReviewImageDto> reviewImages = reviewImageService.findByStore(sno);
//            if (reviewImages.size() > 0) {
//                model.addAttribute("reviewImageList", reviewImages);
//                Collections.reverse(reviewImages);
//            }
//
//            return "page/detail";
//        }

        List<ReviewImage> reviewImages = fileService.reviewImageFiles(reviewDto.getImageFiles());
        detailReviewService.saveImageReview(reviewDto.getReviewText(), sno, principalDetails.getBasicUser(), reviewImages);

        List<DetailReviewDto> saveImageResult = detailReviewService.findByStore(sno);
        Collections.reverse(saveImageResult);
        model.addAttribute("memberReview", saveImageResult);

        return "redirect:/page/detail/"+sno;
    }


    //대댓글
    @PostMapping("/page/detail/addReview/{sno}/{rno}")
    public String saveNestedReview(@PathVariable("sno") Long sno,
                                   @PathVariable("rno") Long rno,
                                   @ModelAttribute("nestedReviewForm") DetailReviewDto reviewDto, Model model,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails, HttpServletRequest request) {

        String reviewText = request.getParameter("reviewText");
        detailReviewService.saveNestedReview(reviewText, sno, principalDetails.getBasicUser(), rno);

        model.addAttribute("memberReview", detailReviewService.findByStore(sno));

        return "page/detail :: #reviewList";
    }


    //멤버리뷰 수정
    @ResponseBody
    @PutMapping("/editReview/{rno}")
    public ResponseEntity<Long> modifyReview(@PathVariable("rno") Long rno,
                                             @Validated @RequestBody DetailReviewDto detailReviewDto, BindingResult bindingResult) {
        detailReviewService.modifyReview(detailReviewDto);
        return new ResponseEntity<>(rno, HttpStatus.OK);
    }


    //리뷰 삭제
    @ResponseBody
    @DeleteMapping("/deleteReview/{sno}/{rno}")
    public ResponseEntity<Long> remove(@PathVariable("rno") Long rno,
                                       @PathVariable("sno") Long sno,
                                       @RequestBody DetailReviewDto detailReviewDto, Model model) {
        detailReviewService.remove(rno);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }


    //리뷰 이미지 불러오기
    @ResponseBody
    @GetMapping("/page/detail/images/{filename}")
    private Resource getReviewImages(@PathVariable("filename") String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFullPath(filename));
    }


    //스토어 이미지 불러오기
    @ResponseBody
    @GetMapping("/page/detail/image/{storeName}{storeFileName}")
    private Resource getStoreImages(@PathVariable("storeFileName") String storeFileName,
                                    @PathVariable("storeName") String storeName) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFullPathImage(storeFileName, storeName));
    }


    // 로컬 이미지 삭제
    @ResponseBody
    @DeleteMapping("/{id}/imageDelete")
    public ResponseEntity<Map<String, String>> imageDelete(@PathVariable("id")Long id,
                                                           @RequestBody ReviewImageDto reviewImageDto) {

        String date = reviewImageDto.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        HashMap<String, String> resultMap = new HashMap<>();
        String filePath = reviewImageDto.getFilePath();
        String thumbnailName = "C:/upload/" + date + "/" +reviewImageDto.getThumbnailName();

        reviewImageService.deleteReviewImage(id, filePath, thumbnailName);
        resultMap.put("key", "success");

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }


    //좋아요
    @ResponseBody
    @PostMapping("/detailLikes/{sno}")
    public ResponseEntity<Map<String, Long>> detailLikes(@PathVariable("sno") Long sno,
                                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Map<String, Long> resultMap = new HashMap<>();

        Long cntMemberLike = likesService.cntMemberLike(sno, principalDetails.getId());

        // JSON 형태로 View에 데이터를 전달하기 위해서 key:value로 변경
        resultMap.put("memberCnt", cntMemberLike);

        if (cntMemberLike <= 0) {
            likesService.addLikes(sno, principalDetails.getBasicUser());
            resultMap.put("totalCount", likesService.cntLikes(sno));
        } else {
            likesService.removeLike(sno, principalDetails.getId());
            resultMap.put("totalCount", likesService.cntLikes(sno));
        }

        // Map에 JSON 형태로 담긴 데이터를 Response 해줌
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

}