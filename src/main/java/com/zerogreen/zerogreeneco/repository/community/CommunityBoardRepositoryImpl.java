package com.zerogreen.zerogreeneco.repository.community;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogreen.zerogreeneco.dto.community.CommunityRequestDto;
import com.zerogreen.zerogreeneco.dto.community.CommunityResponseDto;
import com.zerogreen.zerogreeneco.dto.search.SearchCondition;
import com.zerogreen.zerogreeneco.dto.search.SearchType;
import com.zerogreen.zerogreeneco.entity.community.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.zerogreen.zerogreeneco.entity.community.QCommunityBoard.communityBoard;
import static com.zerogreen.zerogreeneco.entity.userentity.QMember.member;

public class CommunityBoardRepositoryImpl implements CommunityBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommunityBoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    QBoardReply subReply = new QBoardReply("subReply");
    QBoardImage subImage = new QBoardImage("subImage");
    QCommunityLike subLike = new QCommunityLike("subLike");

    @Override
    public CommunityResponseDto findDetailBoard(Long boardId) {
        return null;
    }

    /*
     * 커뮤니티 게시판 전체 리스트 (검색)
     * */
    @Override
    public Slice<CommunityResponseDto> findAllCommunityList(Pageable pageable, SearchCondition condition) {

        List<CommunityResponseDto> content =
                dtoProjections(subLike.board.id.eq(
                        communityBoard.id), subReply.board.id.eq(communityBoard.id), subImage.board.id.eq(communityBoard.id))
                        .where(
                                isSearch(condition.getSearchType(), condition.getContent())
                        )
                        .orderBy(communityBoard.createdDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        List<CommunityBoard> countQuery = queryFactory
                .selectFrom(communityBoard)
                .fetch();

        return new PageImpl<>(content, pageable, countQuery.size());
    }

    @Override
    public Slice<CommunityResponseDto> findAllCommunityList(Pageable pageable) {

        List<CommunityResponseDto> content =
                dtoProjections(subLike.board.id.eq(
                        communityBoard.id), subReply.board.id.eq(communityBoard.id), subImage.board.id.eq(communityBoard.id))
                        .orderBy(communityBoard.createdDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        List<CommunityBoard> countQuery = queryFactory
                .selectFrom(communityBoard)
                .fetch();

        return new PageImpl<>(content, pageable, countQuery.size());
    }

    /*
     * 카테고리별 리스트 출력
     * */
    @Override
    public Slice<CommunityResponseDto> findByCategory(Pageable pageable, Category category) {

        List<CommunityResponseDto> content =
                dtoProjections(subLike.board.id.eq(
                        communityBoard.id), subReply.board.id.eq(communityBoard.id), subImage.board.id.eq(communityBoard.id))
                        .where(communityBoard.category.eq(category))
                        .orderBy(communityBoard.createdDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        List<CommunityBoard> countQuery = queryFactory
                .selectFrom(communityBoard)
                .where(communityBoard.category.eq(category))
                .fetch();

        return new PageImpl<>(content, pageable, countQuery.size());
    }

    /*
     * 상세 보기
     * */
    @Override
    public CommunityResponseDto findDetailView(Long id) {

        return dtoProjections(subLike.board.id.eq(id), subReply.board.id.eq(id), subImage.board.id.eq(communityBoard.id))
                .where(communityBoard.id.eq(id))
                .fetchFirst();
    }

//    public CommunityReplyDto replyTest(Long boardId) {
//        return queryFactory
//                .select(Projections.constructor(CommunityReplyDto.class),
//                        subReply.id,
//                        communityBoard.id,
//                        subReply.parentReply.id,
//                        subReply.replyContent,
//                        member.nickname,
//                        member._super.username,
//                        subReply.createdDate
//                        )
//                .from(subReply, subReply)
//                .join(communityBoard, communityBoard)
//                .join(member, member)
//                .where(subReply.board.id.eq(boardId))
//                .orderBy(subReply.parentReply.id.coalesce(subReply.board.id).as(0L))
//    }

    /*
     * 공통 SELECT절
     * */
    private JPAQuery<CommunityResponseDto> dtoProjections(BooleanExpression id, BooleanExpression id1, BooleanExpression id2) {
        return queryFactory
                .select(Projections.constructor(CommunityResponseDto.class,
                        communityBoard.id,
                        communityBoard.text,
                        member.nickname,
                        member.username,
                        member.vegetarianGrade,
                        member.id,
                        communityBoard.category,
                        communityBoard.modifiedDate,
                        communityBoard.isChat,
                        communityBoard.count,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(subLike.id))
                                        .from(subLike, subLike)
                                        .where(id), "likeCount"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(subReply.id))
                                        .from(subReply, subReply)
                                        .where(id1), "replyCount"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(subImage.thumbnailName)
                                        .from(subImage, subImage)
                                        .where(id2.and(subImage.id.eq(
                                                JPAExpressions
                                                        .select(subImage.id.min())
                                                        .from(subImage, subImage)
                                                        .where(subImage.board.id.eq(communityBoard.id))))), "thumbImage")
                ))
                .from(communityBoard, communityBoard)
                .join(communityBoard.member, member);
    }

    @Override
    public CommunityRequestDto boardModify(Long boardId) {
        return queryFactory
                .select(Projections.constructor(CommunityRequestDto.class,
                        communityBoard.text,
                        communityBoard.category
                ))
                .from(communityBoard, communityBoard)
                .where(communityBoard.id.eq(boardId))
                .fetchFirst();
    }

    /*
     * 게시판 조회수
     * */
    @Override
    public void addViewCount(Long boardId) {
        queryFactory
                .update(communityBoard)
                .set(communityBoard.count, communityBoard.count.add(1))
                .where(communityBoard.id.eq(boardId))
                .execute();
    }

    /*
     * 검색 조건
     * */
    private BooleanExpression eqNickname(String nickname) {
        return StringUtils.hasText(nickname) ? member.nickname.like(nickname) : null;
    }

    private BooleanExpression eqContent(String content) {
        return StringUtils.hasText(content) ? communityBoard.text.containsIgnoreCase(content) : null;
    }

    private BooleanExpression isSearch(SearchType searchType, String searchText) {
        if (searchType.equals(SearchType.CONTENT)) {
            return eqContent(searchText);
        } else if (searchType.equals(SearchType.WRITER)) {
            return eqNickname(searchText);
        } else {
            return eqContent(searchText).or(eqNickname(searchText));
        }
    }
}