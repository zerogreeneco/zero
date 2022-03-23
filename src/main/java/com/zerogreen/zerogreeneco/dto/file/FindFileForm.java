package com.zerogreen.zerogreeneco.dto.file;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindFileForm {

    private Long id;
    private String uploadFileName;
    private String storeFileName;
}
