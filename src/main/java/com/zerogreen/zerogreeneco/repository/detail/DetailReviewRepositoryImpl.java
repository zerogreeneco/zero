package com.zerogreen.zerogreeneco.repository.detail;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogreen.zerogreeneco.dto.detail.DetailReviewDto;
import com.zerogreen.zerogreeneco.entity.detail.QReviewImage;
import com.zerogreen.zerogreeneco.entity.file.QStoreImageFile;

import javax.persistence.EntityManager;
import java.util.List;

import static com.zerogreen.zerogreeneco.entity.detail.QDetailReview.detailReview;
import static com.zerogreen.zerogreeneco.entity.userentity.QBasicUser.basicUser;
import static com.zerogreen.zerogreeneco.entity.userentity.QMember.member;
import static com.zerogreen.zerogreeneco.entity.userentity.QStoreMember.storeMember;


public class DetailReviewRepositoryImpl implements DetailReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public DetailReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    QStoreImageFile subImage = new QStoreImageFile("subImage");
    QReviewImage reviewImage = new QReviewImage("reviewImage");

    //회원별 리뷰 남긴 가게 리스트(memberMyInfo)
    @Override
    public List<DetailReviewDto> getReviewByUser(Long id) {
        List<DetailReviewDto> content = queryFactory
                .select(Projections.constructor(DetailReviewDto.class,
                        detailReview.id,
                        detailReview.reviewText,
                        storeMember.id,
                        storeMember.storeName,
                        basicUser.id,
                        detailReview.createdDate,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(subImage.thumbnailName)
                                        .from(subImage, subImage)
                                        .where(subImage.id.eq(
                                                JPAExpressions
                                                        .select(subImage.id.min())
                                                        .from(subImage, subImage)
                                                        .where(subImage.storeMember.id.eq(storeMember.id)))), "thumbImage")
                        ))
                .from(detailReview, detailReview)
                .innerJoin(detailReview.storeMember, storeMember).on(storeMember.id.eq(detailReview.storeMember.id))
                .innerJoin(detailReview.reviewer, basicUser).on(basicUser.id.eq(detailReview.reviewer.id))
                .orderBy(detailReview.id.desc())
                .limit(10)
                .where(detailReview.reviewer.id.eq(id))
                .fetch();
        return content;
    }


    @Override
    public List<DetailReviewDto> getReviewByStore(Long id) {
        List<DetailReviewDto> reviews = queryFactory
                .select(Projections.constructor(DetailReviewDto.class,
                        member.vegetarianGrade,
                        member.nickname,
                        detailReview.reviewText,
                        detailReview.createdDate,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(reviewImage.thumbnailName)
                                        .from(reviewImage, reviewImage)
                                        .where(reviewImage.id.eq(
                                                JPAExpressions
                                                        .select(reviewImage.id.min())
                                                        .from(reviewImage, reviewImage)
                                                        .where(reviewImage.detailReview.id.eq(detailReview.id)))),"reviewImage")
                                        ))
                .from(detailReview, detailReview)
                .innerJoin(member).on(member.id.eq(detailReview.reviewer.id))
                .where(detailReview.storeMember.id.eq(id))
                .fetch();

        return reviews;
    }
}
