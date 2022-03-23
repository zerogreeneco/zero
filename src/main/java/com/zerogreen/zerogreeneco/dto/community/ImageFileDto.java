package com.zerogreen.zerogreeneco.dto.community;

import com.zerogreen.zerogreeneco.entity.community.BoardImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ImageFileDto {

    private Long id;
    private String storeFileName;
    private String thumbnailImage;
    private String uploadFileName;
    private String filePath;

    public ImageFileDto(BoardImage image) {
        this.id = image.getId();
        this.storeFileName = image.getStoreFileName();
        this.thumbnailImage = image.getThumbnailName();
        this.uploadFileName = image.getUploadFileName();
        this.filePath = image.getFilePath();
    }

    public ImageFileDto(String storeFileName) {
        this.thumbnailImage = "thumb_" + storeFileName;
    }
}
