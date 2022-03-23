package com.zerogreen.zerogreeneco.entity.detail;

import com.zerogreen.zerogreeneco.entity.baseentity.BaseTimeEntity;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Lob
    private String reviewText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer")
    private BasicUser reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store")
    private StoreMember storeMember;

    // 셀프 참조로 해당 댓글의 부모 댓글 PK 입력
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_review_id", nullable = true)
    private DetailReview parentReview;

    // 양방향 매핑으로 부모 댓글의 자식 댓글 리스트
    // 조인컬럼을 없애면 테이블이 생겨서 일단 냅둠
    @OneToMany(cascade = ALL)
    @JoinColumn(name = "parentReview")
    private List<DetailReview> nestedReviewList = new ArrayList<>();

    @OneToMany(mappedBy = "detailReview", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<ReviewImage> imageList = new ArrayList<>();

    //계층구분
    private int depth = 1;


    //리뷰 수정
    public void editReview(String reviewText) {
        this.reviewText = reviewText;
    }

    //기본 리뷰 추가
    public DetailReview(String reviewText, BasicUser reviewer, StoreMember storeMember) {
        this.reviewText = reviewText;
        this.reviewer = reviewer;
        this.storeMember = storeMember;
    }

    // 자식 댓글일 경우 부모 댓글에 리스트 형태로 추가하는 메서드
    public void addNestedReview(DetailReview nestedReview) {
        nestedReview.parentReview = this;
        // 자식 댓글이 저장되면 기본 depth 1을 2로 변경
        nestedReview.depth = this.depth + 1;
        this.nestedReviewList.add(nestedReview);
    }

}
