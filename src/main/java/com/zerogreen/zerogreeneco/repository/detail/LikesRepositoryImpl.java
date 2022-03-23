package com.zerogreen.zerogreeneco.repository.detail;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogreen.zerogreeneco.dto.detail.LikesDto;
import com.zerogreen.zerogreeneco.entity.file.QStoreImageFile;

import javax.persistence.EntityManager;
import java.util.List;

import static com.zerogreen.zerogreeneco.entity.detail.QLikes.likes;
import static com.zerogreen.zerogreeneco.entity.userentity.QBasicUser.basicUser;
import static com.zerogreen.zerogreeneco.entity.userentity.QStoreMember.storeMember;


public class LikesRepositoryImpl implements LikesRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public LikesRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    QStoreImageFile subImage = new QStoreImageFile("subImage");

    @Override
    public List<LikesDto> getLikesByUser(Long id) {
        List<LikesDto> content = queryFactory
                .select(Projections.constructor(LikesDto.class,
                        likes.id,
                        storeMember,
                        basicUser,
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
                .from(likes, likes)
                .innerJoin(likes.storeMember, storeMember).on(storeMember.id.eq(likes.storeMember.id))
                .innerJoin(likes.basicUser, basicUser).on(basicUser.id.eq(likes.basicUser.id))
                .orderBy(likes.id.desc())
                .limit(10)
                .where(likes.basicUser.id.eq(id))
                .fetch();
        return content;
    }

}
