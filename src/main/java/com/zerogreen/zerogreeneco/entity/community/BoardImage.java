package com.zerogreen.zerogreeneco.entity.community;

import com.zerogreen.zerogreeneco.entity.baseentity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_image_id")
    private Long id;

    private String uploadFileName;
    private String storeFileName;
    private String thumbnailName;
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private CommunityBoard board;

    // 파일 폴더 저장 (FileService)
    public BoardImage(String uploadFileName, String storeFileName, String filePath, String thumbnailName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.thumbnailName = thumbnailName;
        this.filePath = filePath;
    }

    // 파일 DB 저장
    public BoardImage(String uploadFileName, String storeFileName, String filePath, CommunityBoard board, String thumbnailName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.thumbnailName = thumbnailName;
        this.filePath = filePath;
        this.board = board;
    }
}
