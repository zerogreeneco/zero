package com.zerogreen.zerogreeneco.entity.file;

import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String fileName;
    private String storeFileName;
    private String thumbnailName;
    private String filePath;
    private String thumbPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private StoreMember storeMember;

    public StoreImageFile(String fileName, String storeFileName, String thumbnailName, String filePath, String thumbPath) {
        this.fileName = fileName;
        this.storeFileName = storeFileName;
        this.thumbnailName = thumbnailName;
        this.filePath = filePath;
        this.thumbPath = thumbPath;
    }

    //DB 저장
    public StoreImageFile(String fileName, String storeFileName, String thumbnailName,  String filePath, String thumbPath, StoreMember storeMember) {
        this.fileName = fileName;
        this.storeFileName = storeFileName;
        this.thumbnailName = thumbnailName;
        this.filePath = filePath;
        this.thumbPath = thumbPath;
        this.storeMember = storeMember;
    }
}
