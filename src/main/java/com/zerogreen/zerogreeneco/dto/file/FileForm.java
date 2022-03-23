package com.zerogreen.zerogreeneco.dto.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class FileForm {

    private Long fileId;
    private String filename;
    private MultipartFile attachFile;
    private List<MultipartFile> files;

}
