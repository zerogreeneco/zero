package com.zerogreen.zerogreeneco.dto.community;

import com.sun.istack.NotNull;
import com.zerogreen.zerogreeneco.entity.community.Category;
import com.zerogreen.zerogreeneco.entity.userentity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
@ToString
public class CommunityRequestDto {

    @NotBlank
    private String text;
    @NotNull
    private Category category;
    private Member writer;
    private boolean chat;
    private List<MultipartFile> imageFiles;

    public CommunityRequestDto(String text, Category category) {
        this.text = text;
        this.category = category;
//        this.chat = chat;
    }

}
