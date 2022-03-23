package com.zerogreen.zerogreeneco.entity.detail;

import com.zerogreen.zerogreeneco.entity.baseentity.BaseTimeEntity;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ReviewImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_image_id")
    private Long id;

    private String uploadFileName;
    private String reviewFileName;
    private String filePath;
    private String thumbnailName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private DetailReview detailReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreMember storeMember;


    // 파일 폴더 저장
    public ReviewImage(String uploadFileName, String reviewFileName, String filePath, String thumbnailName) {
        this.uploadFileName = uploadFileName;
        this.reviewFileName = reviewFileName;
        this.filePath = filePath;
        this.thumbnailName = thumbnailName;
    }

    // 파일 DB 저장
    public ReviewImage(String uploadFileName, String reviewFileName, String filePath,
                       DetailReview detailReview, StoreMember storeMember, String thumbnailName) {
        this.uploadFileName = uploadFileName;
        this.reviewFileName = reviewFileName;
        this.filePath = filePath;
        this.detailReview = detailReview;
        this.storeMember = storeMember;
        this.thumbnailName = thumbnailName;
    }

}
