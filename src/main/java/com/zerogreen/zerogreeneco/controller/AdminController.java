package com.zerogreen.zerogreeneco.controller;

import com.zerogreen.zerogreeneco.dto.paging.PagingDto;
import com.zerogreen.zerogreeneco.dto.paging.RequestPageSortDto;
import com.zerogreen.zerogreeneco.dto.store.NonApprovalStoreDto;
import com.zerogreen.zerogreeneco.entity.file.RegisterFile;
import com.zerogreen.zerogreeneco.repository.file.RegisterFileRepository;
import com.zerogreen.zerogreeneco.service.file.FileService;
import com.zerogreen.zerogreeneco.service.user.BasicUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final BasicUserService basicUserService;
    private final FileService fileService;
    private final RegisterFileRepository registerFileRepository;

    @GetMapping("/approvalStore")
    public Page<NonApprovalStoreDto> approvalStore(Model model, RequestPageSortDto requestPageDto) {

        Pageable pageable = requestPageDto.getPageableSort(Sort.by("createdDate").descending());

        Page<NonApprovalStoreDto> nonApprovalStore = basicUserService.findByNonApprovalStore(pageable);

        PagingDto result = new PagingDto(nonApprovalStore);

        model.addAttribute("result", result);

        return nonApprovalStore;
    }

    /*
     * ?????? ?????? ????????????
     * */
    @GetMapping("/approvalStore/attach/{fileId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable("fileId") Long fileId) throws MalformedURLException {

        RegisterFile registerFile = registerFileRepository.findById(fileId).orElseThrow();
        String uploadFileName = registerFile.getUploadFileName();
        String storeFileName = registerFile.getStoreFileName();

        UrlResource urlResource = new UrlResource("file:" + fileService.getFullPath(storeFileName));

        String encodeUploadFilename = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8); // ????????? ??? ??????????????? ?????? ??? ?????? ?????????
        // CONTENT_DISPOSITION : HttpBody??? ???????????? ???????????? ????????? ???????????? ??????
        // attachment; filename="?????????"??? body??? ?????? ?????? ???????????? ???????????? ??????
        String contentDisposition = "attachment; filename=\"" + encodeUploadFilename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    /*
     * UNSTORE -> STORE ?????? ??????
     * */
    @PostMapping("/approve")
    public String changeUserRole(@RequestParam(value = "memberId[]", defaultValue = "0") List<Long> memberId, Model model, RequestPageSortDto requestPageDto) {

        final long unAuthUserCnt = 0L;

        if (memberId.contains(unAuthUserCnt)) {
            model.addAttribute("msg", "??????????????? ????????? ????????? ??????????????????.");
            return "admin/approvalStore";
        }

        basicUserService.changeStoreUserRole(memberId);
        model.addAttribute("msg", "????????? ?????????????????????. ");
        Pageable pageable = requestPageDto.getPageableSort(Sort.by("createdDate").descending());

        Page<NonApprovalStoreDto> nonApprovalStore = basicUserService.findByNonApprovalStore(pageable);

        PagingDto result = new PagingDto(nonApprovalStore);

        model.addAttribute("result", result);
        return "admin/approvalStore :: #approval-list";
    }

    @GetMapping("/search")
    public void searchNonApprovalStore(@Validated @ModelAttribute("search") NonApprovalStoreDto searchCond,
                                       BindingResult bindingResult, RequestPageSortDto requestPageDto) {

        Pageable pageable = requestPageDto.getPageableSort(Sort.by("memberID").descending());

        basicUserService.nonApprovalStoreSearch(searchCond, pageable);
    }
}
