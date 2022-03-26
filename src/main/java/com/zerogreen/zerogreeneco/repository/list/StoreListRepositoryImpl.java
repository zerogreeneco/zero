package com.zerogreen.zerogreeneco.repository.list;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogreen.zerogreeneco.dto.search.SearchCondition;
import com.zerogreen.zerogreeneco.dto.search.StoreSearchType;
import com.zerogreen.zerogreeneco.dto.store.StoreDto;
import com.zerogreen.zerogreeneco.entity.file.QStoreImageFile;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import com.zerogreen.zerogreeneco.entity.userentity.StoreType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.zerogreen.zerogreeneco.entity.detail.QLikes.likes;
import static com.zerogreen.zerogreeneco.entity.file.QStoreImageFile.storeImageFile;
import static com.zerogreen.zerogreeneco.entity.userentity.QStoreMember.storeMember;
import static com.zerogreen.zerogreeneco.entity.userentity.QStoreMenu.storeMenu;
import static com.zerogreen.zerogreeneco.entity.userentity.UserRole.STORE;


public class StoreListRepositoryImpl implements StoreListRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public StoreListRepositoryImpl(EntityManager manager) {
        this.jpaQueryFactory = new JPAQueryFactory(manager);
    }

    @Override
    public Slice<StoreDto> getShopList(Pageable pageable) {
        List<StoreDto> shopList =
                shopProjections()
                        .where(storeMember._super.userRole.eq(STORE),
                                storeMember.storeType.eq(StoreType.ECO_SHOP))
                        .groupBy(storeMember.id)
                        .orderBy(likes.id.count().desc().nullsLast())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        List<StoreMember> countQuery = jpaQueryFactory
                .selectFrom(storeMember)
                .fetch();

        return new PageImpl<>(shopList, pageable, countQuery.size());
    }

    @Override
    public Slice<StoreDto> getShopSearchList(Pageable pageable, SearchCondition searchCondition) {
        List<StoreDto> shopList =
                shopProjections()
                        .where(storeMember._super.userRole.eq(STORE),
                                storeMember.storeType.eq(StoreType.ECO_SHOP),
                                isSearch(searchCondition.getStoreSearchType(), searchCondition.getContent()))
                        .groupBy(storeMember.id)
                        .orderBy(likes.id.count().desc().nullsLast())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        List<StoreMember> countQuery = jpaQueryFactory
                .selectFrom(storeMember)
                .fetch();

        return new PageImpl<>(shopList, pageable, countQuery.size());
    }

    @Override
    public Slice<StoreDto> getFoodList(Pageable pageable) {
        List<StoreDto> foodList = foodProjections()
                .where(storeMember._super.userRole.eq(STORE),
                        storeMember.storeType.ne(StoreType.ECO_SHOP))
                .groupBy(storeMember.id)
                .orderBy(likes.id.count().desc().nullsLast())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<StoreMember> countQuery = jpaQueryFactory
                .selectFrom(storeMember)
                .fetch();

        return new PageImpl<>(foodList, pageable, countQuery.size());
    }

    @Override
    public Slice<StoreDto> getFoodTypeList(Pageable pageable, StoreType storeType) {
        List<StoreDto> foodList = foodProjections()
                .where(storeMember._super.userRole.eq(STORE),
                        storeMember.storeType.ne(StoreType.ECO_SHOP),
                        storeMember.storeType.eq(storeType))
                .groupBy(storeMember.id, storeMember.storeName)
                .orderBy(likes.id.count().desc().nullsLast())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<StoreMember> countQuery = jpaQueryFactory
                .selectFrom(storeMember)
                .fetch();

        return new PageImpl<>(foodList, pageable, countQuery.size());
    }

    @Override
    public Slice<StoreDto> getFoodSearchList(Pageable pageable, SearchCondition searchCondition) {
        List<StoreDto> foodList = foodProjections()
                .where(storeMember._super.userRole.eq(STORE),
                        storeMember.storeType.ne(StoreType.ECO_SHOP),
                        isSearch(searchCondition.getStoreSearchType(), searchCondition.getContent()))
                .groupBy(storeMember.id)
                .orderBy(likes.id.count().desc().nullsLast())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<StoreMember> countQuery = jpaQueryFactory
                .selectFrom(storeMember)
                .fetch();

        return new PageImpl<>(foodList, pageable, countQuery.size());
    }

    private JPAQuery<StoreDto> shopProjections() {
        return jpaQueryFactory
                .select(Projections.constructor(StoreDto.class,
                        storeMember.id,
                        storeMember.storeName,
                        storeMember.storeInfo.storePhoneNumber,
                        storeMember.storeInfo.openTime,
                        storeMember.storeInfo.closeTime,
                        likes.id.count(),
                        storeMenu.menuName.min(),
                        storeImageFile.thumbnailName.min()))
                .from(storeMember, storeMember)
                .leftJoin(likes).on(likes.storeMember.id.eq(storeMember.id))
                .leftJoin(storeMenu).on(storeMenu.storeMember.id.eq(storeMember.id))
                .leftJoin(storeImageFile).on(storeImageFile.storeMember.id.eq(storeMember.id));
    }

    private JPAQuery<StoreDto> foodProjections() {
        return jpaQueryFactory
                .select(Projections.constructor(StoreDto.class,
                        storeMember.id,
                        storeMember.storeName,
                        storeMember.storeInfo.storePhoneNumber,
                        storeMember.storeInfo.openTime,
                        storeMember.storeInfo.closeTime,
                        likes.id.count(),
                        storeMenu.menuName.min(),
                        storeImageFile.thumbnailName.min()))
                .from(storeMember, storeMember)
                .leftJoin(likes).on(likes.storeMember.id.eq(storeMember.id))
                .leftJoin(storeMenu).on(storeMenu.storeMember.id.eq(storeMember.id))
                .leftJoin(storeImageFile).on(storeImageFile.storeMember.id.eq(storeMember.id));
    }

    /*
     * 검색 조건
     * */
    private BooleanExpression eqStoreName(String storeName) {
        return StringUtils.hasText(storeName) ? storeMember.storeName.containsIgnoreCase(storeName) : null;
    }

    private BooleanExpression eqMenuName(String menuName) {
        return StringUtils.hasText(menuName) ? storeMenu.menuName.containsIgnoreCase(menuName) : null;
    }

    private BooleanExpression isSearch(StoreSearchType searchType, String searchText) {
        if (searchType.equals(StoreSearchType.store_name)) {
            return eqStoreName(searchText);
        } else if (searchType.equals(StoreSearchType.item)) {
            return eqMenuName(searchText);
        } else {
            return eqMenuName(searchText).or(eqStoreName(searchText));
        }
    }

}