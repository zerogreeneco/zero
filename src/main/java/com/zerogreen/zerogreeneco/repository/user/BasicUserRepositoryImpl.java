package com.zerogreen.zerogreeneco.repository.user;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogreen.zerogreeneco.dto.store.NonApprovalStoreDto;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;
import com.zerogreen.zerogreeneco.entity.userentity.QBasicUser;
import com.zerogreen.zerogreeneco.entity.userentity.QStoreMember;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.zerogreen.zerogreeneco.entity.file.QRegisterFile.registerFile;
import static com.zerogreen.zerogreeneco.entity.userentity.QBasicUser.basicUser;


public class BasicUserRepositoryImpl implements BasicUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BasicUserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private final QBasicUser asBasicUser = basicUser;
    private final QStoreMember asStoreMember = asBasicUser.as(QStoreMember.class);

    @Override
    public Long findByAuthUsername(String username) {
        return queryFactory
                .select(basicUser.count())
                .from(basicUser)
                .where(basicUser.username.eq(username))
                .fetchFirst();
    }

    /*
     * 비승인 가게 회원 조회 (승인용)
     * */
    @Override
    public Page<NonApprovalStoreDto> findByUnApprovalStore(Pageable pageable) {

        List<NonApprovalStoreDto> content = queryFactory
                .select(Projections.constructor(NonApprovalStoreDto.class,
                        asStoreMember._super.id
                        , asStoreMember._super.username
                        , asStoreMember.storeName
                        , asStoreMember.storeInfo.storePhoneNumber
                        , asStoreMember.storeInfo.storeAddress
                        , asStoreMember.storeRegNum
                        , registerFile.id
                        , registerFile.uploadFileName
                ))
                .from(asStoreMember)
                .innerJoin(asStoreMember.registerFile, registerFile)
                .where(asStoreMember._super.userRole.eq(UserRole.UN_STORE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<BasicUser> countQuery = queryFactory
                .selectFrom(basicUser)
                .where(basicUser.userRole.eq(UserRole.UN_STORE));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<NonApprovalStoreDto> findByUnApprovalStoreSearch(Pageable pageable, String searchType, String searchText) {
        List<NonApprovalStoreDto> content = queryFactory
                .select(Projections.constructor(NonApprovalStoreDto.class,
                        asStoreMember._super.id
                        , asStoreMember._super.username
                        , asStoreMember.storeName
                        , asStoreMember.storeInfo.storePhoneNumber
                        , asStoreMember.storeInfo.storeAddress
                        , asStoreMember.storeRegNum
                        , registerFile.id
                        , registerFile.uploadFileName
                ))
                .from(asStoreMember)
                .innerJoin(asStoreMember.registerFile, registerFile)
                .where(
                        asStoreMember._super.userRole.eq(UserRole.UN_STORE),
                        isSearch(searchType, searchText)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(basicUser.id.count())
                .from(basicUser, basicUser)
                .where(basicUser.userRole.eq(UserRole.UN_STORE))
                .fetchFirst();

        return new PageImpl<>(content, pageable, count);
    }

    /*
     * 비승인 가게 회원 USER_ROLE 변경 (승인용)
     * */
    @Override
    public void changeUserRole(List<Long> memberId) {
        queryFactory
                .update(basicUser)
                .set(basicUser.userRole, UserRole.STORE)
                .where(basicUser.id.in(memberId))
                .execute();
    }

    /*
     * 검색 조건 및 페이징
     * */
    @Override
    public Page<NonApprovalStoreDto> searchAndPaging(NonApprovalStoreDto condition, Pageable pageable) {
        List<NonApprovalStoreDto> content = queryFactory
                .select(Projections.constructor(NonApprovalStoreDto.class
                        , asStoreMember.storeName
                        , asStoreMember.storeInfo.storePhoneNumber
                        , asStoreMember.storeInfo.storeAddress
                ))
                .from(asStoreMember)
                .innerJoin(asStoreMember.registerFile, registerFile)
                .where(
                        usernameContains(condition.getUsername()),
                        storeNameContains(condition.getStoreName()),
                        storeRegNumContains(condition.getStoreRegNum())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<BasicUser> countQuery = queryFactory
                .selectFrom(basicUser)
                .where(basicUser.userRole.eq(UserRole.UN_STORE));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression storeRegNumContains(String storeRegNum) {
        return StringUtils.hasText(storeRegNum) ? asStoreMember.storeRegNum.contains(storeRegNum) : null;
    }

    private BooleanExpression storeNameContains(String storeName) {
        return StringUtils.hasText(storeName) ? asStoreMember.storeName.contains(storeName) : null;
    }

    private BooleanExpression usernameContains(String username) {
        return StringUtils.hasText(username) ? asBasicUser.username.contains(username) : null;
    }

    private BooleanExpression isSearch(String searchType, String searchText) {
        if (searchType.equals("storeId")) {
            return usernameContains(searchText);
        } else if (searchType.equals("storeName")) {
            return storeNameContains(searchText);
        } else if (searchType.equals("regNum")) {
            return storeRegNumContains(searchText);
        } else {
            return null;
        }
    }
}
