package com.zerogreen.zerogreeneco.repository.user;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogreen.zerogreeneco.dto.store.NonApprovalStoreDto;
import com.zerogreen.zerogreeneco.dto.store.StoreDto;
import com.zerogreen.zerogreeneco.entity.detail.QDetailReview;
import com.zerogreen.zerogreeneco.entity.detail.QLikes;
import com.zerogreen.zerogreeneco.entity.userentity.QBasicUser;
import com.zerogreen.zerogreeneco.entity.userentity.QStoreMember;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.zerogreen.zerogreeneco.entity.userentity.QBasicUser.basicUser;
import static com.zerogreen.zerogreeneco.entity.userentity.QStoreMember.storeMember;
import static com.zerogreen.zerogreeneco.entity.userentity.UserRole.USER;

public class StoreMemberRepositoryImpl implements StoreMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public StoreMemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private final QBasicUser asBasicUser = basicUser;
    private final QStoreMember asStoreMember = asBasicUser.as(QStoreMember.class);

    @Override
    public List<NonApprovalStoreDto> findByApprovalStore(UserRole userRole) {
        List<NonApprovalStoreDto> content = queryFactory
                .select(Projections.constructor(NonApprovalStoreDto.class,
                        asStoreMember.id
                        ,asStoreMember.storeName
                        ,asStoreMember.storeInfo.storeAddress
                        ,asStoreMember.storeType
                ))
                .from(asStoreMember)
                .where(asStoreMember._super.userRole.eq(userRole.STORE))
                .fetch();
        return content;
    }

    //스토어 db (Detail)
    @Override
    public StoreDto getStoreById(Long sno) {
        QLikes subLike = new QLikes("subLike");
        QDetailReview subReview = new QDetailReview("subReview");
            return queryFactory
                .select(Projections.constructor(StoreDto.class,
                        storeMember.id,
                        storeMember.storeName,
                        storeMember.storeType,
                        storeMember.storeInfo,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(subLike.id))
                                        .from(subLike, subLike)
                                        .where(subLike.storeMember.id.eq(sno)),"likesCount"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(subReview.id))
                                        .from(subReview, subReview)
                                        .where(subReview.storeMember.id.eq(sno)
                                                .and(subReview.reviewer.userRole.eq(USER)) ),"reviewCnt")
                        ))
                .from(storeMember, storeMember)
                .where(storeMember.id.eq(sno))
                .fetchFirst();
    }

}
