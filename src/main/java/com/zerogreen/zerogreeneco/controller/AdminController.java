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
     * 첨부 파일 다운로드
     * */
    @GetMapping("/approvalStore/attach/{fileId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable("fileId") Long fileId) throws MalformedURLException {

        RegisterFile registerFile = registerFileRepository.findById(fileId).orElseThrow();
        String uploadFileName = registerFile.getUploadFileName();
        String storeFileName = registerFile.getStoreFileName();

        UrlResource urlResource = new UrlResource("file:" + fileService.getFullPath(storeFileName));

        String encodeUploadFilename = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8); // 한글로 된 파일이름이 꺠질 수 있기 때문에
        // CONTENT_DISPOSITION : HttpBody에 들어오는 컨텐츠의 성향을 알려주는 속성
        // attachment; filename="파일명"은 body에 오는 값을 다운로드 받아라는 의미
        String contentDisposition = "attachment; filename=\"" + encodeUploadFilename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

//    /*
//     * UNSTORE -> STORE 권한 변경
//     * */
//    @PostMapping("/approve")
//    @ResponseBody
//    public ResponseEntity<Map<String, String>> changeUserRole(@RequestParam(value = "memberId[]", defaultValue = "0") List<Long> memberId) {
//        Map<String, String> resultMap = new HashMap<>();
//        if (memberId.contains(0L)) {
//            resultMap.put("result", "fail");
//            return new ResponseEntity<>(resultMap, HttpStatus.OK);
//        }
//        log.info("APPROVAL MEMBERID LIST={}", memberId);
//        log.info("APPROVAL MEMBERID LIST={}", memberId.size());
//        basicUserService.changeStoreUserRole(memberId);
//        resultMap.put("result", "success");
//        return new ResponseEntity<>(resultMap, HttpStatus.OK);
//    }

    /*
     * UNSTORE -> STORE 권한 변경
     * */
    @PostMapping("/approve")
    public String changeUserRole(@RequestParam(value = "memberId[]", defaultValue = "0") List<Long> memberId, Model model, RequestPageSortDto requestPageDto) {

        if (memberId.contains(0L)) {
            model.addAttribute("msg", "회원가입을 승인할 회원을 체크해주세요.");
            return "admin/approvalStore";
        }

        basicUserService.changeStoreUserRole(memberId);
        model.addAttribute("msg", "승인이 완료되었습니다. ");
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
